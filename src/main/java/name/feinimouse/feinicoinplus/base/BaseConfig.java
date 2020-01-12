package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.base.node.BaseCenter;
import name.feinimouse.feinicoinplus.base.node.BaseOrder;
import name.feinimouse.feinicoinplus.base.node.BaseVerifier;
import name.feinimouse.feinicoinplus.core.NodeConfig;
import name.feinimouse.feinicoinplus.core.node.BaseNode;
import name.feinimouse.feinicoinplus.core.node.FetchCenter;
import name.feinimouse.feinicoinplus.core.node.Node;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;

@Configuration
public class BaseConfig extends BaseConfigPrototype implements NodeConfig, InitializingBean {
    
    private Node initNode(BaseNode node) {
        String address = addressManager.getAddress();
        KeyPair keyPair = signGenerator.genKeyPair();
        publicKeyHub.setKey(address, keyPair.getPublic());
        node.setAddress(address);
        node.setPrivateKey(keyPair.getPrivate());
        node.setNetwork(nodeNetwork);
        node.setTaskInterval(NODE_INTERVAL);
        nodeNetwork.registerNode(node);
        return node;
    }
    
    @Override
    @Bean
    public Node order() {
        BaseOrder order = new BaseOrder();
        order.setVerifiersAddress(verifier().getAddress());
        return initNode(order);
    }

    @Override
    @Bean
    public Node verifier() {
        BaseVerifier verifier = new BaseVerifier(verifierCore);
        return initNode(verifier);
    }

    @Override
    @Bean
    public Node center() {
        FetchCenter center = new BaseCenter(centerCore);
        center.setFetchInterval(FETCH_INTERVAL);
        center.setOrdersAddress(order().getAddress());
        return initNode(center);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SimpleTransGen simpleTransGen = (SimpleTransGen) transactionGenerator;
        simpleTransGen.setAddress(addressManager.getAddress());
    }
}
