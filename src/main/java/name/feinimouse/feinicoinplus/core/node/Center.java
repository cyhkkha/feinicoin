package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.exception.*;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.utils.StopwatchExecutor;
import name.feinimouse.utils.TimerResult;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Center extends AutoStopNode {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Setter
    @PropNeeded
    protected CenterContext centerContext;
    @Setter
    @PropNeeded
    protected HashGenerator hashGen;
    @Setter
    @PropNeeded
    protected ConsensusNetwork consensusNetwork;

    public Center() {
        super(NODE_CENTER);
        transCache = new ConcurrentLinkedQueue<>();
    }

    @PropNeeded
    @Setter
    protected String ordersAddress;
    @PropNeeded
    @Setter
    protected PrivateKey privateKey;

    // 为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置fetch时间
    protected long fetchTime = 3 * 100;
    @Setter
    protected long periodTime = 1000;
    @Setter
    protected long fetchInterval = 10;
    
    private Queue<Carrier> transCache;

    @Override
    protected void afterWork() {
        super.afterWork();
        transCache.clear();
    }

    @Override
    protected void gapWorking() throws NodeRunningException {
        // 定时拉取并处理
        StopwatchExecutor fetcher = new StopwatchExecutor(
            fetchInterval, fetchTime, stopper -> {
            if (isStop()) {
                stopper.stop();
                return;
            }
            try {
                transactionLoop();
                assetTransLoop();
            } catch (ControllableException e) {
                logger.warn(e.getMessage());
                resetGap();
            } catch (TransAdmitFailedException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                resetGap();
            }
        });
        fetcher.start();

        // 生产区块
        TimerResult<Block> produceResult = TimerUtils.run(this::produceBlock);
        Block block = produceResult.get();
        long produceTime = produceResult.getTime();

        // 同步并存储区块
        TimerResult<?> syncResult = TimerUtils.run(this::synchronizeBlock, block);
        long syncTime = syncResult.getTime();

        // 至少留100ms的时间用于拉取，否则抛出异常
        if (periodTime < syncTime + produceTime + 100) {
            throw new NodeRunningException("sync or produce timeout: "
                + "[sync: " + syncTime + ", produce: " + produceTime + "]");
        }

        fetchTime = periodTime - syncTime + produceTime;
    }

    // 生产区块
    private Block produceBlock() {
        if (isStop()) {
            return null;
        }
        Packer[] transArr = transCache.stream().map(Carrier::getPacker).toArray(Packer[]::new);
        transCache.clear();
        Block block = centerContext.pack(hashGen.hash(transArr, Transaction.class), address);
        resetGap();
        return block;
    }

    // 同步区块
    private void synchronizeBlock(Block block) {
        if (isStop() || block == null) {
            return;
        }
        try {
            // 同步完，获取联合签名后的区块
            Packer consensusResult = consensusNetwork.signAndCommit(privateKey, block);
            resetGap();
            // 若同步成功则提交写入
            if (consensusResult != null) {
                centerContext.admit(consensusResult);
                resetGap();
            }
        } catch (ConsensusException | DaoException e) {
            e.printStackTrace();
            resetGap();
        }
    }

    /////////////////
    //   拉   取   //
    /////////////////
    private Carrier fetchValidTrans(Class<?> tClass) throws ControllableException {
        Carrier fetchCarrier = genCarrier(ordersAddress, MSG_FETCH_ORDER, null);
        try {
            Carrier carrier = fetchFromNetWork(fetchCarrier, tClass);
            if (carrier == null) {
                return null;
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
            if (packer.getEnter() == null
                || packer.getVerifier() == null
                || packer.getOrder() == null
                || attachM.getVerifiedResult() == null) {
                throw new BadRequestException("Invalid attach message");
            }

            // 必须存在验证者的签名，且必须验证通过
            if (packer.excludeSign(packer.getVerifier())
                || attachM.getVerifiedResult()) {
                throw new ControllableException("Failed verification");
            }
            return carrier;
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new ControllableException("Fetch format is incorrect");
        }
    }

    private void transactionLoop() throws ControllableException, TransAdmitFailedException {
        Carrier carrier = fetchValidTrans(Transaction.class);
        if (carrier == null) {
            return;
        }
        Transaction transaction = (Transaction) carrier.getPacker().obj();
        logger.trace("已拉取普通交易({})", transaction);
        centerContext.commit(transaction);
        transCache.add(carrier);
        resetGap();
    }

    private void assetTransLoop() throws ControllableException, TransAdmitFailedException {
        Carrier carrier = fetchValidTrans(AssetTrans.class);
        if (carrier == null) {
            return;
        }
        AssetTrans assetTrans = (AssetTrans) carrier.getPacker().obj(); 
        logger.trace("已拉取资产交易({})", assetTrans);
        centerContext.commit(carrier.getPacker());
        resetGap();
    }

}
