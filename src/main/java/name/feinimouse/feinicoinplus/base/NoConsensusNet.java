package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.ConsensusNetwork;

import java.security.PrivateKey;

public class NoConsensusNet implements ConsensusNetwork {
    private SignGenerator signGenerator;
    private HashGenerator hashGenerator;

    public NoConsensusNet(SignGenerator signGenerator, HashGenerator hashGenerator) {
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
