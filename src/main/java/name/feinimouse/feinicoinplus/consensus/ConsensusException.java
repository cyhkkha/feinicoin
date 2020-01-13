package name.feinimouse.feinicoinplus.consensus;

import name.feinimouse.feinicoinplus.core.exception.NodeException;

public class ConsensusException extends NodeException {
    public ConsensusException() {
    }

    public ConsensusException(String message) {
        super(message);
    }
}
