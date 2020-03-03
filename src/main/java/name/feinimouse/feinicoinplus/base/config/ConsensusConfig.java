package name.feinimouse.feinicoinplus.base.config;

import name.feinimouse.feinicoinplus.base.consensus.ClassicalConNode;
import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNode;
import name.feinimouse.feinicoinplus.base.consensus.OptimizedConNode;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNode;
import name.feinimouse.feinicoinplus.consensus.AbstractBFTNode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyPair;

@Configuration
public class ConsensusConfig extends BaseConfig {
    
    /////////////////////
    // 老的共识节点实现 //
    /////////////////////
    private <T extends ClassicalConNode> void setupNode(T node) {
        node.setNet(conNodeNet);

        String address = addressManager.getAddress();
        node.setAddress(address);

        KeyPair keyPair = signGenerator.genKeyPair();
        publicKeyHub.setKey(address, keyPair.getPublic());
        node.setPrivateKey(keyPair.getPrivate());

        node.setPublicKeyHub(publicKeyHub);

        node.setSignGenerator(signGenerator);

    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例
    public OptimizedConNode optimizedConNode() {
        OptimizedConNode optimizedConNode = new OptimizedConNode();
        setupNode(optimizedConNode);
        optimizedConNode.setHashGenerator(hashGenerator);
        return optimizedConNode;
    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ClassicalConNode classicalConNode() {
        ClassicalConNode classicalConNode = new ClassicalConNode();
        setupNode(classicalConNode);
        return classicalConNode;
    }


    /////////////////////
    // 新的共识节点实现 //
    /////////////////////
    
    private void setupBFTNode(AbstractBFTNode node) {
        String address = addressManager.getAddress();
        KeyPair keyPair = signGenerator.genKeyPair();
        publicKeyHub.setKey(address, keyPair.getPublic());

        node.setAddress(address);
        node.setPublicKeyHub(publicKeyHub);
        node.setPrivateKey(keyPair.getPrivate());
        node.setSignGenerator(signGenerator);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PBFTNode pbftNode() {
        PBFTNode pbftNode = new PBFTNode();
        setupBFTNode(pbftNode);
        return pbftNode;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DPOS_BFTNode dpos_bftNode() {
        DPOS_BFTNode dpos_bftNode = new DPOS_BFTNode();
        setupBFTNode(dpos_bftNode);
        return dpos_bftNode;
    }
}
