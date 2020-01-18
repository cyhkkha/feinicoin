package name.feinimouse.feinicoinplus.consensus;

public interface ConNode {
    String TYPE_CALLBACK = "CALLBACK";
    String TYPE_CONSENSUS = "CONSENSUS";
    String TYPE_CONFIRM = "CONFIRM";

    String getAddress();

    void consensus(ConMessage message) throws ConsensusException;

    void callback(ConMessage message) throws ConsensusException;

    void confirm(ConMessage message) throws ConsensusException;
}
