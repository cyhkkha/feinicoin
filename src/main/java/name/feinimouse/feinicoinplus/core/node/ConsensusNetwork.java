package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.exception.ConsensusException;

import java.security.PrivateKey;

public interface ConsensusNetwork {
    Packer signAndCommit(PrivateKey key, Block block) throws ConsensusException;
}
