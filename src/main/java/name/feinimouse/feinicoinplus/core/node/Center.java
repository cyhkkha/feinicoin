package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.block.*;
import name.feinimouse.feinicoinplus.core.data.AttachMessage;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.exception.*;
import name.feinimouse.feinicoinplus.core.lambda.RunnerStopper;

import java.security.PrivateKey;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Center extends AutoStopNode {
    protected Queue<Packer> transCache;

    @Getter
    @Setter
    @PropNeeded
    protected CenterContext content;
    @Setter
    @Getter
    @PropNeeded
    protected String ordersAddress;
    @Setter
    @Getter
    @PropNeeded
    protected DaoManager daoManager;
    @Setter
    @Getter
    @PropNeeded
    protected SignGen signGen;
    @Setter
    @Getter
    @PropNeeded
    protected HashGen hashGen;
    @Setter
    @Getter
    @PropNeeded
    protected PublicKeyHub publicKeyHub;

    protected PrivateKey privateKey;

    // 为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置fetch时间
    protected int fetchTime = 3 * 100;
    protected int lastSynchronizeTime = 8 * 100;

    public Center(PrivateKey privateKey) {
        super(NODE_CENTER);
        this.privateKey = privateKey;
        transCache = new ConcurrentLinkedQueue<>();
    }

    @Override
    protected void afterWork() {
        transCache.clear();
    }

    @Override
    protected void working() throws NodeRunningException, NodeStopException {
        // TODO
        super.working();
    }

    /////////////////
    //   拉   取   //
    /////////////////
    protected Carrier fetchValidTrans(Class<?> tClass) throws ControllableException {
        Carrier fetchCarrier = genCarrier(ordersAddress, MSG_FETCH_ORDER, null);
        try {
            Carrier carrier = fetchFromNetWork(fetchCarrier, tClass);
            if (carrier == null) {
                throw new ControllableException("fetch failed");
            }

            Packer packer = carrier.getPacker();
            // 如果fetch的结果不含packer，且packer的类型不符则丢弃
            if (!Optional.ofNullable(packer)
                .map(Packer::objClass)
                .map(c -> c.equals(tClass))
                .orElse(false)) {
                throw new ControllableException("Invalid fetch result");
            }

            AttachMessage attachM = carrier.getAttachMessage();
            // 必须存在各级操作者的信息
            if (attachM.getEnter() == null
                || attachM.getVerifier() == null
                || attachM.getOrder() == null
                || attachM.getVerifiedResult() == null) {
                throw new BadCommitException("Invalid attach message");
            }
            
            // 必须存在验证者的签名，且必须验证通过
            if (packer.excludeSign(attachM.getVerifier())
                || attachM.getVerifiedResult()) {
                throw new ControllableException("Failed verification");
            }
            return carrier;
        } catch (BadCommitException e) {
            e.printStackTrace();
            throw new ControllableException("Fetch format is incorrect");
        }
    }

    protected void transactionLoop(RunnerStopper stopper) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(Transaction.class);
            Packer packer = carrier.getPacker();
            Transaction transaction = (Transaction) packer.obj();
            content.admitTransaction(transaction);
            transCache.add(packer);
        } catch (ControllableException | TransAdmitFailedException e) {
            e.printStackTrace();
        }
    }

    protected void assetTransLoop(RunnerStopper stopper) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(AssetTrans.class);
            Packer packer = carrier.getPacker();
            AssetTrans assetTrans = (AssetTrans) packer.obj();
            content.admitAssetTrans(assetTrans);
        } catch (ControllableException | TransAdmitFailedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "commit");
    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "fetch");
    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "commit");
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "fetch");
    }
}
