package name.feinimouse.feinicoinplus.core.exception;

import name.feinimouse.feinicoinplus.core.Node;
import name.feinimouse.feinicoinplus.core.data.Carrier;

public class BadCommitException extends Exception {
    public BadCommitException() {
        super();
    }

    public BadCommitException(String message) {
        super(message);
    }
    
    public <T> BadCommitException(Class<T> tClass) {
        super("Can't resolve class: " + tClass.toString());
    }
    
    public static BadCommitException notWorkException(Node node) {
        return new BadCommitException("Node Dose Not Work, Commit Failed from: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException noSenderException(Node node) {
        return new BadCommitException("Commit without origin, Commit Failed from: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException classNotSupportException(Carrier carrier, Node node) {
        return new BadCommitException("Error Operation type of " + carrier.gainType() 
            + " , Operation Failed from: " + node.nodeMsg().toString());
    }
    public static BadCommitException commitNotSupportException(Node node) {
        return new BadCommitException("commit not support from: " + node.nodeMsg().toString());
    }
}
