package name.feinimouse.feinicoinplus.base.config;

import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNode;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNode;
import name.feinimouse.feinicoinplus.consensus.AbstractBFTNode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyPair;

@Configuration
public class ConsensusConfig extends BaseConfig {
    
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
