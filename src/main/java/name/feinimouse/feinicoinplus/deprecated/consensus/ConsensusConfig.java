package name.feinimouse.feinicoinplus.deprecated.consensus;

import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyPair;

@Configuration
@ComponentScan({"name.feinimouse.feinicoinplus.deprecated.consensus", "name.feinimouse.feinicoinplus.base"})
public class ConsensusConfig {

    @Autowired
    protected AddressManager addressManager;
    @Autowired
    protected ConNodeNet conNodeNet;
    @Autowired
    protected SignGenerator signGenerator;
    @Autowired
    protected PublicKeyHub publicKeyHub;
    @Autowired
    protected HashGenerator hashGenerator;

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
}
