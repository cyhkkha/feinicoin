package name.feinimouse.feinicoinplus.base.sim;

import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNet;
import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNode;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNet;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNode;
import name.feinimouse.feinicoinplus.consensus.AbstractBFTNet;
import name.feinimouse.feinicoinplus.consensus.AbstractBFTNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.SimResult;
import name.feinimouse.feinicoinplus.core.node.AbstractNode;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.sim.NodeManager;
import name.feinimouse.lambda.CustomRunner;
import name.feinimouse.lambda.ReturnRunner;
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
    public void preRunConsensus() {
        runConsensusCircle(5, 20
            , 10, 30000
            , () -> (PBFTNet) applicationContext.getBean("pbftNet")
            , () -> (PBFTNode) applicationContext.getBean("pbftNode"));
    }

    // 区块链试验次数
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

    // 共识实验次数
    @Value("${CON_RUN_TIMES}")
    private int CON_RUN_TIMES;
    // 共识节点开始数量
    @Value("${CON_NODE_START_NUM}")
    private int CON_NODE_START_NUM;
    // 共识节点递增数量
    @Value("${CON_NODE_INCREASE_NUM}")
    private int CON_NODE_INCREASE_NUM;
    // 最长共识时间
    @Value("${CON_MAX_TIME}")
    private int CON_MAX_TIME;

    // 按照配置发送一定比例的随机混合交易到传统模式
    public SimResult sendRandomMixTransClassical(double rate) {
        logger.info("-------- 开始传统节点实验，资产比例：{} --------", rate);
        return sendRandomMixTransClassical(SIM_RUN_TIMES, rate);
    }

    public SimResult sendRandomMixTransClassical(int runTimes, double rate) {
        return runNodeCircle(runTimes, SIM_TRANS_START_NUM, SIM_TRANS_INCREASE_NUM
            , nodeManager -> {
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
        return runNodeCircle(runTimes, SIM_TRANS_START_NUM, SIM_TRANS_INCREASE_NUM
            , nodeManger -> {
                nodeManger.startFetchNode();
                return nodeManger.getOrder();
            }, node -> {
                Packer packer = transactionGenerator.genRandomMixTrans(rate);
                return transactionGenerator.genCarrier(packer, node.getAddress());
            });
    }

    // 测试PBFT
    public SimResult runPBFT() {
        logger.info("-------- 开始进行 PBFT 实验--------");
        return runConsensusCircle(CON_RUN_TIMES, CON_NODE_START_NUM, CON_NODE_INCREASE_NUM, CON_MAX_TIME
            , () -> (PBFTNet) applicationContext.getBean("pbftNet")
            , () -> (PBFTNode) applicationContext.getBean("pbftNode"));
    }

    // 测试DPOS_BFT
    public SimResult runDPOS_BFT() {
        logger.info("-------- 开始进行 DPOS_BFT 实验--------");
        return runConsensusCircle(CON_RUN_TIMES, CON_NODE_START_NUM, CON_NODE_INCREASE_NUM, CON_MAX_TIME
            , () -> (DPOS_BFTNet) applicationContext.getBean("dpos_bftNet")
            , () -> (DPOS_BFTNode) applicationContext.getBean("dpos_bftNode"));
    }

    public SimResult runConsensusCircle(int runTimes, int startNodeNum
        , int increaseNodeNum, long maxConsensusTime
        , ReturnRunner<AbstractBFTNet> netRunner
        , ReturnRunner<AbstractBFTNode> nodeRunner) {

        SimResult simResult = new SimResult();
        simResult.setName("共识时间表");
        simResult.setXName("节点数量");
        simResult.setYName("时间消耗");

        // 实验的重复次数
        for (int i = 0; i < runTimes; i++) {
            // 获取单例网络
            AbstractBFTNet bftNet = netRunner.run();
            // 节点加入网络
            int nodeNum = startNodeNum + i * increaseNodeNum;
            for (int j = 0; j < nodeNum; j++) {
                bftNet.putNode(nodeRunner.run());
            }
            // 创建消息
            BFTMessage bftMessage = new BFTMessage();
            bftMessage.setMessage("test ss ss ss");
            
            long startTime = System.currentTimeMillis();
            // 开始共识
            bftNet.start(bftMessage);
            // 观察共识是否结束
            while (!bftNet.isConsensus()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() - startTime > maxConsensusTime) {
                    logger.error("共识超时：{} ms", maxConsensusTime);
                    break;
                }
            }
            long time = System.currentTimeMillis() - startTime;
            logger.info("共识结束，{} 个节点，共识时间：{} ms", nodeNum, time);
            simResult.put(String.valueOf(nodeNum), String.valueOf(time));
            // 销毁网络
            bftNet.destroy();
        }
        return simResult;
    }

    public SimResult runNodeCircle(int runTimes, int startTransNum, int increaseTransNum
        , CustomRunner<NodeManager, AbstractNode> init
        , CustomRunner<Node, Carrier> generator) {

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
            final int count = startTransNum + i * increaseTransNum;
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
