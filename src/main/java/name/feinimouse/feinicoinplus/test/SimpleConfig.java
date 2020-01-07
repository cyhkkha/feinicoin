package name.feinimouse.feinicoinplus.test;

import name.feinimouse.feinicoinplus.base.SimpleCenter;
import name.feinimouse.feinicoinplus.base.SimpleOrder;
import name.feinimouse.feinicoinplus.base.SimpleVerifier;
import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.security.KeyPair;

@Configuration
@PropertySource("classpath:feinicoinplus-config.properties")
public class SimpleConfig {
    private AddressManager addressManager;
    private SignGenerator signGenerator;
    private CenterContext centerContext;
    private PublicKeyHub publicKeyHub;
    private HashGenerator hashGenerator;
    private ConsensusNetwork consensusNetwork;

    @Bean
    public Verifier verifier() {
        Verifier verifier = new SimpleVerifier(publicKeyHub, signGenerator);
        verifier.setAddress(addressManager.getAddress());
        KeyPair vKeys = signGenerator.genKeyPair();
        verifier.setPrivateKey(vKeys.getPrivate());
        publicKeyHub.setKey(verifier.getAddress(), vKeys.getPublic());
        return verifier;
    }

    @Bean
    public Order order() {
        Order order = new SimpleOrder();
        order.setAddress(addressManager.getAddress());
        order.setAddress(addressManager.getAddress());
        order.setVerifiersAddress(verifier().getAddress());
        return order;
    }

    @Bean
    public Center center() {
        Center center = new SimpleCenter(centerContext, hashGenerator, consensusNetwork);
        center.setAddress(addressManager.getAddress());
        center.setOrdersAddress(order().getAddress());
        KeyPair cKey = signGenerator.genKeyPair();
        center.setPrivateKey(cKey.getPrivate());
        publicKeyHub.setKey(center.getAddress(), cKey.getPublic());
        return center;
    }

    @Autowired
    public void setAddressManager(AddressManager addressManager) {
        this.addressManager = addressManager;
    }

    @Autowired
    public void setSignGenerator(SignGenerator signGenerator) {
        this.signGenerator = signGenerator;
    }

    @Autowired
    public void setCenterContext(CenterContext centerContext) {
        this.centerContext = centerContext;
    }

    @Autowired
    public void setPublicKeyHub(PublicKeyHub publicKeyHub) {
        this.publicKeyHub = publicKeyHub;
    }

    @Autowired
    public void setHashGenerator(HashGenerator hashGenerator) {
        this.hashGenerator = hashGenerator;
    }

    @Autowired
    public void setConsensusNetwork(ConsensusNetwork consensusNetwork) {
        this.consensusNetwork = consensusNetwork;
    }

    //    // hash机
//    @Bean
//    public HashGenerator hashGen() {
//        return new MurmurHashGen(hashSeed, hashLong);
//    }
//    
//    // 签名机
//    @Bean
//    public SignGenerator signGen() {
//        return new SM2SignGen();
//    }

    // 公钥仓库
//    @Bean
//    public PublicKeyHub publicKeyHub() {
//        return new PublicKeyHub();
//    }
//    
//    // 验证节点，非单例的prototype
//    @Bean @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public Node verifier() {
//        PrivateKey key = signGen().genKeyPair().getPrivate();
//        Verifier node = new Verifier(key);
//        node.setSignGen(signGen());
//        node.setPublicKeyHub(publicKeyHub());
//        return node;
//    }
}
