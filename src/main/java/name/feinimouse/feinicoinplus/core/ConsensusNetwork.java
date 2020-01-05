package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.exception.ConsensusException;

public interface ConsensusNetwork {
    boolean commit(Packer blockPacker) throws ConsensusException;
}
