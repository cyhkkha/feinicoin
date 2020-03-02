package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.AbstractNode;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.sim.NodeManager;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import name.feinimouse.lambda.CustomRunner;
import name.feinimouse.utils.LoopUtils;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("simRunner")
@ComponentScan("name.feinimouse.feinicoinplus.base")
@PropertySource("classpath:feinicoinplus-config-test.properties")
public class BaseRunner implements SimRunner {
    //////////////////////////////////////////////////////////////////
    // 可能会用到的的注解：
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例的bean注解
    // @ComponentScan("name.feinimouse.feinicoinplus.base")
    // @PropertySource("classpath:feinicoinplus-config-test.properties")
    // @Value("${coin.hash.seed}")
    //////////////////////////////////////////////////////////////////

    protected Logger logger = LogManager.getLogger(BaseRunner.class);

    protected ApplicationContext applicationContext;
    protected TransactionGenerator transactionGenerator;

    // 试验次数
    public static final int RUN_TIMES = 20;
    // 起始发送的交易次数
    public static final int TRANS_START_NUM = 100;
    // 交易递增次数
    public static final int TRANS_INCREASE_NUM = 100;
    // 重试次数
    public static final int RETRY_TIMES = 50;
    // 发送间隔
    public static final int SEND_INTERVAL = 10;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setTransactionGenerator(TransactionGenerator transactionGenerator) {
        this.transactionGenerator = transactionGenerator;
    }

    @Override
    public ResultManager start(InitParam initParam) {
        

        return null;
    }

    public void sendRandomMixTransClassical(double rate) {
        runCircle(nodeManager -> {
            nodeManager.startClassicalNode();
            return nodeManager.getClassicalCenter();
        }, node -> {
            Packer packer = transactionGenerator.genRandomMixTrans(rate);
            Carrier carrier = transactionGenerator.genCarrier(packer, node.getAddress());
            carrier.getNetInfo().setMsgType(Node.MSG_COMMIT_CENTER);
            return carrier;
        });
    }

    public void SendRandomMixTransFetch(double rate) {
        runCircle(nodeManger -> {
            nodeManger.startFetchNode();
            return nodeManger.getOrder();
        }, node -> {
            Packer packer = transactionGenerator.genRandomMixTrans(rate);
            return transactionGenerator.genCarrier(packer, node.getAddress());
        });
    }

    public void runCircle(CustomRunner<NodeManager, AbstractNode> init, CustomRunner<Node, Carrier> generator) {
        // 实验的重复次数
        for (int i = 0; i < RUN_TIMES; i++) {
            NodeManager nodeManager = (NodeManager)
                applicationContext.getBean("nodeManager");
            final AbstractNode node = init.run(nodeManager);
            final int count = TRANS_START_NUM + i * TRANS_INCREASE_NUM;
            long time = TimerUtils.run(() -> {
                // 交易的发送次数
                for (int j = 0; j < count; j++) {
                    try {
                        LoopUtils.loopExec(RETRY_TIMES, SEND_INTERVAL,
                            () -> node.commit(generator.run(node)));
                    } catch (Exception e) {
                        logger.error("交易提交失败");
                        e.printStackTrace();
                    }
                }
                try {
                    node.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            logger.info("处理{}笔交易，总计运行时间 {} ms", count, time);
        }
    }

}
