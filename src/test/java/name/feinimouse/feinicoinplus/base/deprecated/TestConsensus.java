package name.feinimouse.feinicoinplus.base.deprecated;

import name.feinimouse.feinicoinplus.base.BaseTest;
import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNet;
import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNode;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNet;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.deprecated.consensus.ClassicalConNode;
import name.feinimouse.feinicoinplus.deprecated.consensus.ConMessage;
import name.feinimouse.feinicoinplus.deprecated.consensus.ConNode;
import name.feinimouse.feinicoinplus.deprecated.consensus.ConNodeNet;
import name.feinimouse.utils.LoopUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.security.KeyPair;
import java.security.PrivateKey;

@ContextConfiguration(classes = name.feinimouse.feinicoinplus.deprecated.consensus.ConsensusConfig.class)
public class TestConsensus extends BaseTest {
    Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    ConNodeNet conNodeNet;
    @Autowired
    AddressManager addressManager;
    @Autowired
    PublicKeyHub publicKeyHub;
    @Autowired
    SignGenerator signGenerator;
    @Autowired
    ApplicationContext context;
    
    @Value("${CON_NODE_START_NUM}")
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
    
    
}
