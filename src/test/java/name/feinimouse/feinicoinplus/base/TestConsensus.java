package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.base.consensus.BaseConNode;
import name.feinimouse.feinicoinplus.base.consensus.ConNodeNet;
import name.feinimouse.feinicoinplus.consensus.ConMessage;
import name.feinimouse.feinicoinplus.consensus.ConNode;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
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
        ConNode conNode1 = (ConNode) context.getBean("conNode");
        ConNode conNode2 = (ConNode) context.getBean("conNode");
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
    
    @Test
    public void testConNodeNet() throws InterruptedException {
        for (int i = 0; i < NODE_NUM; i ++) {
            BaseConNode conNode = (BaseConNode) context.getBean("conNode");
            conNode.setNodeNum(NODE_NUM);
            conNodeNet.putNode(conNode);
        }
        long now = System.currentTimeMillis();
        conNodeNet.consensus(genConMessage());
        Thread.sleep(1000);
        while (!conNodeNet.isConfirm()) {
            Thread.sleep(1000);
            if (System.currentTimeMillis() - now > 10 * 1000) {
                logger.info("超时");
                break;
            }
        }
        logger.info("耗时 {}ms", System.currentTimeMillis() - now);
    }
}
