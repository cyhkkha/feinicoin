package name.feinimouse.feinicoinplus.test;

import name.feinimouse.feinicoinplus.base.SimpleCenter;
import name.feinimouse.feinicoinplus.base.SimpleOrder;
import name.feinimouse.feinicoinplus.base.SimpleVerifier;
import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.security.KeyPair;

@Configuration
@PropertySource("classpath:feinicoinplus-config.properties")
public class SimpleConfig {
    private final AddressManager addressManager;
    private final SignGenerator signGenerator;
    private final CenterContext centerContext;
    private final PublicKeyHub publicKeyHub;
    private final HashGenerator hashGenerator;
    private final ConsensusNetwork consensusNetwork;
    private final AccountManager accountManager;
    private final AssetManager assetManager;

    // 非单例的prototype
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

    @Autowired
    public SimpleConfig(AddressManager addressManager
        , SignGenerator signGenerator, CenterContext centerContext
        , PublicKeyHub publicKeyHub, HashGenerator hashGenerator
        , ConsensusNetwork consensusNetwork, AccountManager accountManager
        , AssetManager assetManager) {
        this.addressManager = addressManager;
        this.signGenerator = signGenerator;
        this.centerContext = centerContext;
        this.publicKeyHub = publicKeyHub;
        this.hashGenerator = hashGenerator;
        this.consensusNetwork = consensusNetwork;
        this.accountManager = accountManager;
        this.assetManager = assetManager;
        init();
    }

    private void init() {
        accountManager.genAccount(100);
        assetManager.genAsset(50);
    }

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
    
}
