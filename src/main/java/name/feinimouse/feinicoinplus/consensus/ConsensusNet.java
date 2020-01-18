package name.feinimouse.feinicoinplus.consensus;

import lombok.Getter;

import java.util.ArrayList;

public class ConsensusNet implements ConNode {
    
    @Getter
    private String address = "0000_0000_0000_0000";
    
    private ArrayList<ConNode> nodeList;
    
    @Override
    public void consensus(ConMessage message) throws ConsensusException {
        
    }

    @Override
    public void callback(ConMessage message) throws ConsensusException {

    }

    @Override
    public void confirm(ConMessage message) throws ConsensusException {

    }
}
