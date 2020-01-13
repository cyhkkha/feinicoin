package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.consensus.ConsensusException;

public interface ConsensusNetwork {
    Packer commit(Packer packer) throws ConsensusException;
}
