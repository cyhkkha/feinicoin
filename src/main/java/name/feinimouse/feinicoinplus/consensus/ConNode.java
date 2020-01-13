package name.feinimouse.feinicoinplus.consensus;

public interface ConNode {
    void consensus(ConMessage message) throws ConsensusException;
    void callback(ConMessage message) throws ConsensusException;
    void confirm(ConMessage message) throws ConsensusException;
}
