package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.feinicoinplus.core.exception.RequestNotSupportException;
import name.feinimouse.lambda.InputRunner;
import name.feinimouse.lambda.RunnerStopper;
import name.feinimouse.utils.StopwatchUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ClassicalCenter extends CacheNode {
    private Logger logger = LogManager.getLogger(ClassicalCenter.class);

    @PropNeeded
    @Setter
    protected VerifierCore verifierCore;

    @PropNeeded
    @Setter
    protected CenterCore centerCore;

    // 处理时间，为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置
    protected long collectTime = 3 * 100;
    // 生产周期
    @Setter
    protected long periodTime = 1000;
    @Setter
    protected long collectInterval = 5;

    public ClassicalCenter(CenterCore centerCore, VerifierCore verifierCore) {
        super(NODE_CENTER_CLASSICAL);
        this.centerCore = centerCore;
        this.verifierCore = verifierCore;
        // 缓存的默认大小
        setCacheWaitMax(50);
    }


    @Override
    protected void gapWorking() throws NodeRunningException {
        AtomicInteger transNum = new AtomicInteger(0);
        AtomicInteger assetTransNum = new AtomicInteger(0);
        var collectResult = StopwatchUtils.run(
            collectTime, collectInterval, stopper -> {
                collectLoop(Transaction.class, this::handlerTrans, stopper, transNum);
                collectLoop(AssetTrans.class, this::handlerAssetTrans, stopper, assetTransNum);
            });

        // 若缓存中已经没有交易则不生产区块，同时跳过间隔时间的重置
        if (transNum.get() + assetTransNum.get() <= 0) {
            try {
                logger.warn("交易等待持续了 {}ms ，未获得交易", collectResult.getTotalRunTime());
                long sleepTime = periodTime - collectResult.getTotalRunTime();
                Thread.sleep(sleepTime > 0 ? sleepTime : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopNode();
            }
            return;
        }
        logger.trace("处理交易持续了 {}ms", collectResult.getTotalRunTime());
        logger.trace("共处理普通交易: {}个，资产交易： {}个", transNum.get(), assetTransNum.get());

        // 执行区块生产
        long consumeTime = centerCore.executeProduce(address, privateKey);
        
        // 优先保障区块生成
//        // 至少留100ms的时间用于其他处理，否则抛出异常
//        if (periodTime < consumeTime + 100) {
//            throw new NodeRunningException("produce timeout !!");
//        }

        // 根据同步时间和生产时间，更新下次的拉取时间
        collectTime = periodTime - consumeTime;
        collectTime = collectTime > 0 ? collectTime : 0;

        // 重置间隔时间
        resetGap();
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadRequestException {
        super.beforeCommit(carrier);
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.notMatch(NODE_ENTER, MSG_COMMIT_CENTER)) {
            throw new RequestNotSupportException(this, netInfo, "not support");
        }
        Order.checkCommit(carrier);
    }

    // 处理缓存的操作
    private void collectLoop(Class<?> c, InputRunner<Carrier> handler
        , RunnerStopper stopper, AtomicInteger transNum) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        Carrier carrier = cacheWait.poll(c);
        if (carrier != null) {
            handler.run(carrier);
            // 拉取数量加一
            transNum.getAndIncrement();
        }
    }

    protected void handlerTrans(Carrier carrier) {
        Packer packer = carrier.getPacker();
        if (verifierCore.verifyTransaction(packer)) {
            verifierCore.sign(privateKey, packer, address);
            packer.setOrder(address);
            packer.setVerifier(address);
            centerCore.handleTransaction(carrier);
        }
    }

    protected void handlerAssetTrans(Carrier carrier) {
        Packer packer = carrier.getPacker();
        if (verifierCore.verifyAssetTrans(packer)) {
            verifierCore.sign(privateKey, packer, address);
            packer.setOrder(address);
            packer.setVerifier(address);
            centerCore.handleAssetTrans(carrier);
        }
    }

}
