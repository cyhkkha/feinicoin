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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("simRunner")
@ComponentScan("name.feinimouse.feinicoinplus.base")
@PropertySource("classpath:feinicoinplus-config-base.properties")
public class BaseRunner implements SimRunner {
    //////////////////////////////////////////////////////////////////
    // 可能会用到的的注解：
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例的bean注解
    // @ComponentScan("name.feinimouse.feinicoinplus.base")
    // @PropertySource("classpath:feinicoinplus-config-base.properties")
    // @Value("${coin.hash.seed}")
    //////////////////////////////////////////////////////////////////

    protected Logger logger = LogManager.getLogger(BaseRunner.class);

    protected ApplicationContext applicationContext;
    protected TransactionGenerator transactionGenerator;

    // 试验次数
    @Value("${SIM_RUN_TIMES}")
    private int SIM_RUN_TIMES;
    // 起始发送的交易次数
    @Value("${SIM_TRANS_START_NUM}")
    private int SIM_TRANS_START_NUM;
    // 交易递增次数
    @Value("${SIM_TRANS_INCREASE_NUM}")
    private int SIM_TRANS_INCREASE_NUM;
    // 重试次数
    @Value("${SIM_RETRY_TIMES}")
    private int SIM_RETRY_TIMES;
    // 发送间隔
    @Value("${SIM_SEND_INTERVAL}")
    private int SIM_SEND_INTERVAL;

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
        logger.info("----------这里是预实验--------");
        preRun();
        logger.info("----------预实验结束----------");
        
//        sendRandomMixTransClassical(0);
//        sendRandomMixTransClassical(1);
//        sendRandomMixTransClassical(0.2);
//        sendRandomMixTransFetch(0.2);
        sendRandomMixTransClassical(0.5);
//        sendRandomMixTransFetch(0.5);

        return null;
    }

    // 按照配置发送一定比例的随机混合交易到传统模式
    public void sendRandomMixTransClassical(double rate) {
        logger.info("-------- 开始传统节点实验，资产比例：{} --------", rate);
        sendRandomMixTransClassical(SIM_RUN_TIMES, rate);
    }
    public void sendRandomMixTransClassical(int runTimes, double rate) {
        runCircle(runTimes, nodeManager -> {
            nodeManager.startClassicalNode();
            return nodeManager.getClassicalCenter();
        }, node -> {
            Packer packer = transactionGenerator.genRandomMixTrans(rate);
            Carrier carrier = transactionGenerator.genCarrier(packer, node.getAddress());
            carrier.getNetInfo().setMsgType(Node.MSG_COMMIT_CENTER);
            return carrier;
        });
    }
    
    // 按照配置发送一定比例的随机混合交易到改进模式
    public void sendRandomMixTransFetch(double rate) {
        logger.info("-------- 开始改进节点实验，资产比例：{} --------", rate);
        sendRandomMixTransFetch(SIM_RUN_TIMES, rate);
    }
    public void sendRandomMixTransFetch(int runTimes, double rate) {
        runCircle(runTimes, nodeManger -> {
            nodeManger.startFetchNode();
            return nodeManger.getOrder();
        }, node -> {
            Packer packer = transactionGenerator.genRandomMixTrans(rate);
            return transactionGenerator.genCarrier(packer, node.getAddress());
        });
    }
    
    public void preRun() {
        sendRandomMixTransClassical(1, 0.5);
        sendRandomMixTransFetch(1, 0.5);
    }
    
    public void runCircle(int runTimes, CustomRunner<NodeManager, AbstractNode> init, CustomRunner<Node, Carrier> generator) {
        // 实验的重复次数
        for (int i = 0; i < runTimes; i++) {
            // 生成新的一套节点
            NodeManager nodeManager = (NodeManager)
                applicationContext.getBean("nodeManager");
            final AbstractNode node = init.run(nodeManager);
            // 计算交易发送数量
            final int count = SIM_TRANS_START_NUM + i * SIM_TRANS_INCREASE_NUM;
            long time = TimerUtils.run(() -> {
                // 交易的发送次数
                for (int j = 0; j < count; j++) {
                    try {
                        // 重试执行提交
                        LoopUtils.loopExec(SIM_RETRY_TIMES, SIM_SEND_INTERVAL,
                            () -> node.commit(generator.run(node)));
                    } catch (Exception e) {
                        logger.error("交易提交失败");
                        e.printStackTrace();
                    }
                }
                // 等待节点运行完毕
                nodeManager.waitNode();
                nodeManager.destroy();
            });
            logger.info("处理{}笔交易，总计运行时间 {} ms", count, time);
        }
    }

}
