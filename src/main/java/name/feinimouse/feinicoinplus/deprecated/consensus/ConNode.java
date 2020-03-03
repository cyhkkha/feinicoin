package name.feinimouse.feinicoinplus.deprecated.consensus;

import name.feinimouse.feinicoinplus.consensus.ConsensusException;

public interface ConNode {
    String TYPE_CALLBACK = "CALLBACK";
    String TYPE_CONSENSUS = "CONSENSUS";
    String TYPE_CONFIRM = "CONFIRM";
    
    String ADDRESS_NET = "0000_0000_0000_0000";

    String getAddress();

    void consensus(ConMessage message) throws ConsensusException;

    void callback(ConMessage message) throws ConsensusException;

    void confirm(ConMessage message) throws ConsensusException;
    
    
    void endRound(ConMessage message);
    
    boolean isConfirm();
}
