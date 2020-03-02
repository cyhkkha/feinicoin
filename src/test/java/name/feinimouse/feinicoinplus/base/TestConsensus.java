package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.base.consensus.BFTNet;
import name.feinimouse.feinicoinplus.base.consensus.ClassicalConNode;
import name.feinimouse.feinicoinplus.base.consensus.ConNodeNet;
import name.feinimouse.feinicoinplus.base.consensus.PBFTConNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import name.feinimouse.feinicoinplus.consensus.ConMessage;
import name.feinimouse.feinicoinplus.consensus.ConNode;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.utils.LoopUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.security.KeyPair;
import java.security.PrivateKey;

public class TestConsensus extends BaseTest {
    @Autowired
    ConNodeNet conNodeNet;
    @Autowired
    AddressManager addressManager;
    @Autowired
    PublicKeyHub publicKeyHub;
    @Autowired
    SignGenerator signGenerator;
    
    @Value("${CONSENSUS_NUM}")
    private int NODE_NUM;
    
    private String address;
    private PrivateKey privateKey;
    
    @Before
    public void before() {
        address = addressManager.getAddress();
        KeyPair keyPair = signGenerator.genKeyPair();
        privateKey = keyPair.getPrivate();
        publicKeyHub.setKey(address, keyPair.getPublic());
    }
    
    @Test
    public void testConNode() {
        ConNode conNode1 = (ConNode) context.getBean("optimizedConNode");
        ConNode conNode2 = (ConNode) context.getBean("optimizedConNode");
        System.out.println(conNode1);
        System.out.println(conNode2);
        Assert.assertNotEquals(conNode1, conNode2);
    }
    
    private ConMessage genConMessage() {
        ConMessage conMessage = new ConMessage(1);
        conMessage.setSender(address);
        conMessage.setHash("TEST");
        conMessage.setType(ConNode.TYPE_CONSENSUS);
        signGenerator.sign(privateKey, conMessage, address);
        return conMessage;
    }
    
    private void testConNodeNet(String nodeName) throws InterruptedException {
        for (int i = 0; i < NODE_NUM; i ++) {
            ClassicalConNode conNode = (ClassicalConNode) context.getBean(nodeName);
            conNode.setNodeNum(NODE_NUM);
            conNodeNet.putNode(conNode);
        }
        long now = System.currentTimeMillis();
        conNodeNet.consensus(genConMessage());
        Thread.sleep(1000);
        while (!conNodeNet.isConfirm()) {
            Thread.sleep(1000);
            if (System.currentTimeMillis() - now > 20 * 1000) {
                logger.info("超时");
                break;
            }
        }
        logger.info("耗时 {}ms", System.currentTimeMillis() - now);
    }
    
    @Test
    public void testOptimizedNet() throws InterruptedException {
        testConNodeNet("optimizedConNode");
    }
    
    @Test
    public void testClassicalNet() throws InterruptedException {
        testConNodeNet("classicalConNode");
    }
    
    @Test
    public void testPBFT() throws InterruptedException {
        BFTNet bftNet= (BFTNet) context.getBean("bftNet");
        LoopUtils.loop(20, () -> {
            PBFTConNode pbftConNode = (PBFTConNode) context.getBean("pbftConNode");
            bftNet.putNode(pbftConNode);
        });
        BFTMessage bftMessage = new BFTMessage();
        bftMessage.setMessage("testssssss");
        long startTime = System.currentTimeMillis();
        bftNet.start(bftMessage);
        while (!bftNet.isConsensus()) {
            Thread.sleep(10);
            if (System.currentTimeMillis() - startTime > 10 * 1000) {
                break;
            }
        }
        logger.info("共识时间：{} ms", System.currentTimeMillis() - startTime);
    }
    
}
