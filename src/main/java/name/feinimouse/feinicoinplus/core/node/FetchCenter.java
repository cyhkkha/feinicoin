package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.node.exception.ControllableException;
import name.feinimouse.feinicoinplus.core.node.exception.NodeRunningException;
import name.feinimouse.feinicoinplus.core.node.exception.TransAdmitFailedException;
import name.feinimouse.lambda.InputRunner;
import name.feinimouse.lambda.RunnerStopper;
import name.feinimouse.utils.StopwatchUtils;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class FetchCenter extends Center {
    private Logger logger = LogManager.getLogger(FetchCenter.class);

    // 为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置fetch时间
    protected long fetchTime = 3 * 100;
    
    // 拉取间隔
    @Setter
    protected long fetchInterval = 5;

    // 拉取地址
    @PropNeeded
    @Setter
    protected String ordersAddress;

    public FetchCenter() {
        super(NODE_CENTER);
    }

    // 工作内容
    @Override
    protected void gapWorking() throws NodeRunningException {
        // 记录节点在周期内是否拉取到交易并处理成功
        AtomicBoolean fetchTag = new AtomicBoolean(true);
        // 定时拉取并处理
        StopwatchUtils.Statistics fetchResult = StopwatchUtils.run(
            fetchTime, fetchInterval, stopper -> {
                fetchLoop(Transaction.class, this::handleTransaction, stopper, fetchTag);
                fetchLoop(AssetTrans.class, this::handleAssetTrans, stopper, fetchTag);
            });

        // 若拉取不到交易则不生产区块，同时跳过间隔时间的重置
        if (fetchTag.get()) {
            try {
                logger.warn("拉取交易持续了 {}ms ，未拉取到交易", fetchResult.getTotalRunTime());
                Thread.sleep(periodTime - fetchResult.getTotalRunTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopNode();
            }
            return;
        }

        logger.trace("拉取交易持续了 {}ms ，预期持续时间为 {}ms 。"
            , fetchResult.getTotalRunTime()
            , fetchTime);

        // 生产区块
        TimerUtils.Result<Block> produceResult = TimerUtils.run(this::produceBlock);
        Block block = produceResult.get();
        long produceTime = produceResult.getTime();

        // 同步并存储区块
        long syncTime = TimerUtils.run(this::synchronizeBlock, block);

        // 至少留100ms的时间用于拉取，否则抛出异常
        if (periodTime < syncTime + produceTime + 100) {
            throw new NodeRunningException("sync or produce timeout: "
                + "[sync: " + syncTime + ", produce: " + produceTime + "]");
        }

        // 根据同步时间和生产时间，更新下次的拉取时间
        fetchTime = periodTime - syncTime + produceTime;

        // 重置间隔时间
        resetGap();
    }

    // 拉取后的操作
    private void fetchLoop(Class<?> fetchClass, InputRunner<Carrier> handler
        , RunnerStopper stopper, AtomicBoolean fetchTag) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(fetchClass);
            if (carrier != null) {
                handler.run(carrier);
                // 如果有一条交易处理成功都将生产区块
                if (fetchTag.get()) {
                    fetchTag.set(false);
                }
            }
        } catch (ControllableException e) {
            logger.info(e.getMessage());
        }
    }
    
    /////////////////
    //   拉   取   //
    /////////////////
    protected Carrier fetchValidTrans(Class<?> tClass) throws ControllableException {
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
                || !attachM.getVerifiedResult()) {
                throw new ControllableException("Failed verification");
            }
            return carrier;
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new ControllableException("Fetch format is incorrect");
        }
    }
    
}
