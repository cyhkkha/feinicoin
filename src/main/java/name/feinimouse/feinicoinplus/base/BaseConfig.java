package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.base.node.BaseCenter;
import name.feinimouse.feinicoinplus.base.node.BaseClasCenter;
import name.feinimouse.feinicoinplus.base.node.BaseOrder;
import name.feinimouse.feinicoinplus.base.node.BaseVerifier;
import name.feinimouse.feinicoinplus.core.node.AbstractNode;
import name.feinimouse.feinicoinplus.core.node.ClassicalCenter;
import name.feinimouse.feinicoinplus.core.node.FetchCenter;
import name.feinimouse.feinicoinplus.core.node.Node;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;

@Configuration
public class BaseConfig extends BaseConfigPrototype implements InitializingBean {
    
    @Value("${NODE_INTERVAL}")
    protected long NODE_INTERVAL;

    @Value("${FETCH_INTERVAL}")
    protected long FETCH_INTERVAL;
    
    @Value("${COLLECT_INTERVAL}")
    protected long COLLECT_INTERVAL;
    
    @Value("${CONSENSUS_TIME}")
    protected long CONSENSUS_TIME;
    
    @Value("${BLOCK_SAVE_TIME}")
    protected long BLOCK_SAVE_TIME;
    
    @Value("${CACHE_CLASSICAL}")
    protected int CACHE_CLASSICAL;
    
    private Node initNode(AbstractNode node) {
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
    
    @Bean
    public Node order() {
        BaseOrder order = new BaseOrder();
        order.setVerifiersAddress(verifier().getAddress());
        return initNode(order);
    }

    @Bean
    public Node verifier() {
        BaseVerifier verifier = new BaseVerifier(verifierCore);
        return initNode(verifier);
    }

    @Bean
    public Node center() {
        FetchCenter center = new BaseCenter(centerCore);
        center.setFetchInterval(FETCH_INTERVAL);
        center.setOrdersAddress(order().getAddress());
        return initNode(center);
    }
    
    @Bean
    public Node classicalCenter() {
        ClassicalCenter center = new BaseClasCenter(centerCore, verifierCore);
        center.setCollectInterval(COLLECT_INTERVAL);
        center.setCacheWaitMax(CACHE_CLASSICAL);
        return initNode(center);
    }

    @Override
    public void afterPropertiesSet() {
        // 初始化交易生成器
        SimpleTransGen simpleTransGen = (SimpleTransGen) transactionGenerator;
        simpleTransGen.setAddress(addressManager.getAddress());
        
        // 初始化共识层
        SleepConsensusNet consensusNet = (SleepConsensusNet) consensusNetwork;
        consensusNet.setConsensusDelay(CONSENSUS_TIME);
        // 初始化存储层
        SleepBlockDao dao = (SleepBlockDao) blockDao;
        dao.setDaoDelay(BLOCK_SAVE_TIME);
    }
}
