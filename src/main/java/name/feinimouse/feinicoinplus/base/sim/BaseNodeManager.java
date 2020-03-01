package name.feinimouse.feinicoinplus.base.sim;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.NodeManager;
import name.feinimouse.lambda.ReturnRunner;
import name.feinimouse.utils.LoopUtils;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component("nodeManager")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseNodeManager implements NodeManager {
    protected Logger logger = LogManager.getLogger(BaseNodeManager.class);


    @Getter
    protected Verifier verifier;
    @Getter
    protected Order order;
    @Getter
    protected FetchCenter fetchCenter;
    @Getter
    protected ClassicalCenter classicalCenter;

    protected TransactionGenerator transactionGenerator;

    @Autowired
    public BaseNodeManager(Verifier verifier, Order order
        , FetchCenter fetchCenter, ClassicalCenter classicalCenter
        , TransactionGenerator transactionGenerator) {
        this.verifier = verifier;
        this.order = order;
        this.fetchCenter = fetchCenter;
        this.classicalCenter = classicalCenter;
        this.transactionGenerator = transactionGenerator;
    }

    public void startFetchNode() {
        order.start();
        verifier.start();
        fetchCenter.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("fetch node 启动成功");
    }

    public void startClassicalNode() {
        classicalCenter.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("classical node 启动成功");
    }
    

    @Override
    public long commitTrans(int count, Node node, ReturnRunner<Carrier> generator) {
        AtomicInteger success = new AtomicInteger();
        long time = TimerUtils.run(() -> LoopUtils.loop(count, () -> {
            try {
                LoopUtils.loopExec(50, 10, () -> node.commit(generator.run()));
                success.getAndIncrement();
            } catch (Exception e) {
                logger.error("交易提交失败");
                e.printStackTrace();
            }
        }));
        logger.info("已发送{}笔普通交易，成功{}笔，用时{}ms", count, success.get(), time);
        return time;
    }
    
    private Carrier getMixTrans(double rate) {
        if (rate > 1 || rate < 0) {
            throw new RuntimeException("rate can not be " + rate);
        }
        Random random = new Random();
        int i = random.nextInt(100);
        Packer packer;
        if (i < rate * 100 - 1) {
            packer = transactionGenerator.genRandomAssetTrans();
        } else {
            packer = transactionGenerator.genRandomTrans();
        }
        return transactionGenerator.genCarrier(packer, classicalCenter.getAddress());
    }
    
    public long sendRandomMixTransClassical(int count, double rate) {
        return commitTrans(count, classicalCenter, () -> {
            Carrier carrier = getMixTrans(rate);
            carrier.getNetInfo().setMsgType(Node.MSG_COMMIT_CENTER);
            return carrier;
        });
    }
    
    public long sendRandomMixTransFetch(int count, double rate) {
        return commitTrans(count, fetchCenter, () -> getMixTrans(rate));
    }
}
