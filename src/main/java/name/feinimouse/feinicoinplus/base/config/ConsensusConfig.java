package name.feinimouse.feinicoinplus.base.config;

import name.feinimouse.feinicoinplus.base.consensus.BaseConNode;
import name.feinimouse.feinicoinplus.consensus.ConNode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyPair;

@Configuration
public class ConsensusConfig extends BaseConfig {
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例
    public ConNode conNode() {
        BaseConNode baseConNode = new BaseConNode();
        baseConNode.setNet(conNodeNet);
        
        String address = addressManager.getAddress();
        baseConNode.setAddress(address);
        
        KeyPair keyPair = signGenerator.genKeyPair();
        publicKeyHub.setKey(address, keyPair.getPublic());
        baseConNode.setPrivateKey(keyPair.getPrivate());
        
        baseConNode.setPublicKeyHub(publicKeyHub);
        
        baseConNode.setSignGenerator(signGenerator);
        
        baseConNode.setHashGenerator(hashGenerator);
        
        return baseConNode;
    }
}
