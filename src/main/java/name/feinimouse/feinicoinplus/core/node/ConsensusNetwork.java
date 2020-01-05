package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.exception.ConsensusException;

public interface ConsensusNetwork {
    boolean commit(Packer blockPacker) throws ConsensusException;
}
