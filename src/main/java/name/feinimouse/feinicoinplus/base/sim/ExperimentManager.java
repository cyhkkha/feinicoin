package name.feinimouse.feinicoinplus.base.sim;

import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.SimResult;
import name.feinimouse.feinicoinplus.core.node.AbstractNode;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.sim.NodeManager;
import name.feinimouse.lambda.CustomRunner;
import name.feinimouse.utils.LoopUtils;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("experimentManager")
public class ExperimentManager {
    private Logger logger = LogManager.getLogger(ExperimentManager.class);

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

    // 预运行
    public long preRunBlockchainNode() {
        return TimerUtils.run(() -> {
            sendRandomMixTransClassical(1, 0.5);
            sendRandomMixTransFetch(1, 0.5);
        });
    }
    
    // 按照配置发送一定比例的随机混合交易到传统模式
    public SimResult sendRandomMixTransClassical(double rate) {
        logger.info("-------- 开始传统节点实验，资产比例：{} --------", rate);
        return sendRandomMixTransClassical(SIM_RUN_TIMES, rate);
    }

    public SimResult sendRandomMixTransClassical(int runTimes, double rate) {
        return runCircle(runTimes, nodeManager -> {
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
    public SimResult sendRandomMixTransFetch(double rate) {
        logger.info("-------- 开始改进节点实验，资产比例：{} --------", rate);
        return sendRandomMixTransFetch(SIM_RUN_TIMES, rate);
    }

    public SimResult sendRandomMixTransFetch(int runTimes, double rate) {
        return runCircle(runTimes, nodeManger -> {
            nodeManger.startFetchNode();
            return nodeManger.getOrder();
        }, node -> {
            Packer packer = transactionGenerator.genRandomMixTrans(rate);
            return transactionGenerator.genCarrier(packer, node.getAddress());
        });
    }

    public SimResult runCircle(int runTimes, CustomRunner<NodeManager, AbstractNode> init, CustomRunner<Node, Carrier> generator) {
        SimResult simResult = new SimResult();
        simResult.setName("运行时间表");
        simResult.setXName("交易数量");
        simResult.setYName("时间消耗");
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
            simResult.put(String.valueOf(count), String.valueOf(time));
        }
        return simResult;
    }
}
