package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.ConsensusNetwork;
import name.feinimouse.feinicoinplus.exception.ConsensusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

@Component("consensusNetwork")
public class SimpleConsenNet implements ConsensusNetwork {
    private final SignGenerator signGenerator;
    private final HashGenerator hashGenerator;

    @Autowired
    public SimpleConsenNet(SignGenerator signGenerator, HashGenerator hashGenerator) {
        this.signGenerator = signGenerator;
        this.hashGenerator = hashGenerator;
    }

    @Override
    public Packer signAndCommit(PrivateKey key, Block block) {
        Packer packer = hashGenerator.hash(block);
        signGenerator.sign(key, packer, "center");
        return packer;
    }
}
