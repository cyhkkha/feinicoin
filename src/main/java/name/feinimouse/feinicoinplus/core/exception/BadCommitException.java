package name.feinimouse.feinicoinplus.core.exception;

import name.feinimouse.feinicoinplus.core.Node;
import name.feinimouse.feinicoinplus.core.data.NodeMessage;
import name.feinimouse.feinicoinplus.core.data.Packer;

public class BadCommitException extends Exception {
    public BadCommitException() {
        super();
    }

    public BadCommitException(String message) {
        super(message);
    }
    
    public static BadCommitException notWorkException(Node node) {
        return new BadCommitException("Node Dose Not Work, Commit Failed from: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException noSenderException(Node node, NodeMessage nodeMessage) {
        return new BadCommitException(
            "Commit without origin: " + nodeMessage.json().toString() + 
                ", Commit Failed from: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException classNotSupportException(Packer packer, Node node) {
        return new BadCommitException("Error Operation type of " + packer.objClass() 
            + " , Operation Failed from: " + node.nodeMsg().toString());
    }
    public static BadCommitException commitNotSupportException(Node node, NodeMessage nodeMessage) {
        return new BadCommitException(
            "commit not support:" + nodeMessage.json().toString() +
                ", from: " + node.nodeMsg().toString());
    }
}
