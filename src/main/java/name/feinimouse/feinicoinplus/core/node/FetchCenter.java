package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.ControllableException;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.lambda.InputRunner;
import name.feinimouse.lambda.RunnerStopper;
import name.feinimouse.utils.StopwatchUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FetchCenter extends AutoStopNode {
    private Logger logger = LogManager.getLogger(FetchCenter.class);
    
    @PropNeeded
    @Setter
    protected CenterCore centerCore;

    // 拉取时间，为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置fetch时间
    protected long fetchTime = 3 * 100;
    // 拉取间隔
    @Setter
    protected long fetchInterval = 5;
    // 生产周期
    @Setter
    protected long periodTime = 1000;

    // 拉取地址
    @PropNeeded
    @Setter
    protected String ordersAddress;

    public FetchCenter(CenterCore centerCore) {
        super(NODE_CENTER);
        this.centerCore = centerCore;
    }

    @Override
    protected void afterWork() {
        super.afterWork();
        centerCore.close();
    }

    // 工作内容
    @Override
    protected void gapWorking() throws NodeRunningException {
        // 记录节点在周期内的拉取数量
        AtomicInteger transNum = new AtomicInteger(0);
        AtomicInteger assetTransNum = new AtomicInteger(0);

        // 定时拉取并处理
        var fetchResult = StopwatchUtils.run(
            fetchTime, fetchInterval, stopper -> {
                fetchLoop(Transaction.class, centerCore::handleTransaction, stopper, transNum);
                fetchLoop(AssetTrans.class, centerCore::handleAssetTrans, stopper, assetTransNum);
            });

        // 若拉取不到交易则不生产区块，同时跳过间隔时间的重置
        if (transNum.get() + assetTransNum.get() <= 0) {
            try {
                logger.warn("拉取交易持续了 {}ms ，未拉取到交易", fetchResult.getTotalRunTime());
                long sleepTime = periodTime - fetchResult.getTotalRunTime();
                Thread.sleep(sleepTime >= 0 ? sleepTime : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopNode();
            }
            return;
        }

        logger.trace("拉取交易持续了 {}ms", fetchResult.getTotalRunTime());
        logger.trace("共拉取普通交易: {}个，资产交易： {}个", transNum.get(), assetTransNum.get());

        // 执行区块生产
        long consumeTime = centerCore.executeProduce(address, privateKey);
        
        // 至少留100ms的时间用于其他处理，否则抛出异常
        if (periodTime < consumeTime + 100) {
            throw new NodeRunningException("produce timeout !!");
        }
        
        // 根据同步时间和生产时间，更新下次的拉取时间
        fetchTime = periodTime - consumeTime;

        // 重置间隔时间
        resetGap();
    }

    // 拉取后的操作
    private void fetchLoop(Class<?> fetchClass, InputRunner<Carrier> handler
        , RunnerStopper stopper, AtomicInteger fetchNum) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(fetchClass);
            if (carrier != null) {
                handler.run(carrier);
                // 拉取数量加一
                fetchNum.getAndIncrement();
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
