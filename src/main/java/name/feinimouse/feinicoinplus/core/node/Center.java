package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.block.*;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.*;
import name.feinimouse.feinicoinplus.core.lambda.RunnerStopper;
import name.feinimouse.utils.StopwatchExecutor;
import name.feinimouse.utils.TimerExecutor;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Center extends AutoStopNode {
    protected Queue<Carrier> transCache;

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
    protected long fetchTime = 3 * 100;
    @Getter @Setter
    protected long periodTime = 1000;
    @Getter @Setter
    protected long fetchInterval = 10;

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

        // 定时拉取并处理
        StopwatchExecutor fetcher = new StopwatchExecutor(fetchInterval, fetchTime, stopper -> {
            transactionLoop(stopper);
            assetTransLoop(stopper);
        });
        fetcher.start();
        
        // 生产区块
        TimerExecutor<Block> producer = new TimerExecutor<>(this::produceBlock);
        Block block = producer.start();
        long produceTime = producer.getRunTime();
        
        // 同步区块
        TimerExecutor<?> synchronizer = new TimerExecutor<>(() -> synchronizeBlock(block));
        synchronizer.start();
        long syncTime = synchronizer.getRunTime();
        
        // 如果100ms的时间都不够拉取，则抛出异常
        if (periodTime < syncTime + produceTime + 100) {
            throw new NodeRunningException("sync or produce timeout: " 
                + "[sync: "+ syncTime + ", produce: "+ produceTime +"]");
        }
        
        fetchTime = periodTime - syncTime + produceTime;
        
        super.working();
    }

    private Block produceBlock() {
        if (isStop()) {
            return null;
        }
        AdmitPackerArr transTree;
        {
            int transSize = transCache.size();
            AdmitPacker[] transArr = new AdmitPacker[transSize];
            String[] summaryArr = new String[transSize];
            for (int i = 0; i < transSize; i++) {
                //noinspection ConstantConditions
                AdmitPacker admitPacker = transCache.poll().admit();
                summaryArr[i] = SummaryUtils.gen(admitPacker);
                transArr[i] = admitPacker;
            }
            transTree = hashGen.hash(transArr, summaryArr, Transaction.class);
        }
        MerkelObj accountTree;
        {
            Account[] accounts = content.getAccounts();
            String[] summaryArr = Arrays.stream(accounts).map(SummaryUtils::gen).toArray(String[]::new);
            accountTree = hashGen.hash(accounts, summaryArr, Account.class);
        }
        
        // TODO
        return null;
    }
    
    
    private void synchronizeBlock(Block block) {
        if (isStop() || block == null) {
            return;
        }
        // TODO
    }
    
    /////////////////
    //   拉   取   //
    /////////////////
    private Carrier fetchValidTrans(Class<?> tClass) throws ControllableException {
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

            AttachInfo attachM = carrier.getAttachInfo();
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

    private void transactionLoop(RunnerStopper stopper) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(Transaction.class);
            Transaction transaction = (Transaction) carrier.getPacker().obj();
            content.admitTransaction(transaction);
            transCache.add(carrier);
            resetGap();
        } catch (ControllableException | TransAdmitFailedException e) {
            e.printStackTrace();
        }
    }

    private void assetTransLoop(RunnerStopper stopper) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(AssetTrans.class);
            AssetTrans assetTrans = (AssetTrans) carrier.getPacker().obj();
            content.admitAssetTrans(assetTrans);
            resetGap();
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
