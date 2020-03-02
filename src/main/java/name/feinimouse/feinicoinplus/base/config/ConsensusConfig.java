package name.feinimouse.feinicoinplus.base.config;

import name.feinimouse.feinicoinplus.base.consensus.ClassicalConNode;
import name.feinimouse.feinicoinplus.base.consensus.OptimizedConNode;
import name.feinimouse.feinicoinplus.base.consensus.PBFTConNode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyPair;

@Configuration
public class ConsensusConfig extends BaseConfig {
    
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
    public PBFTConNode pbftConNode() {
        PBFTConNode pbftConNode = new PBFTConNode();
        String address = addressManager.getAddress();
        KeyPair keyPair = signGenerator.genKeyPair();
        publicKeyHub.setKey(address, keyPair.getPublic());
        
        pbftConNode.setAddress(address);
        pbftConNode.setPublicKeyHub(publicKeyHub);
        pbftConNode.setPrivateKey(keyPair.getPrivate());
        pbftConNode.setSignGenerator(signGenerator);
        
        return pbftConNode;
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
}
