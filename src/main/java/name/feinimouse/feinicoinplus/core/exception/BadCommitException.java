package name.feinimouse.feinicoinplus.core.exception;

import name.feinimouse.feinicoinplus.core.Node;
import name.feinimouse.feinicoinplus.core.data.NodeMessage;

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
    
    public static BadCommitException noSenderException(Node node) {
        return new BadCommitException(
            "Commit without origin, and commit Failed from: " + node.nodeMsg().toString());
    }

    public static BadCommitException commitOverflowException(Node node) {
        return new BadCommitException(
            "Commit Failed, Node cache overflow from: " + node.nodeMsg().toString());
    }

    public static BadCommitException classNotSupportException(Node node, Class<?> aClass) {
        if (aClass == null) {
            return new BadCommitException("Null operation class type, operation failed from: " + node.nodeMsg().toString());
        }
        return new BadCommitException("Error operation class type of " + aClass
            +" , operation failed from: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException commitNotSupportException(Node node, NodeMessage nodeMessage) {
        if (nodeMessage == null) {
            return new BadCommitException(
                "Null commit message from: " + node.nodeMsg().toString());    
        }
        return new BadCommitException(
            "Commit not support:" + nodeMessage.json().toString() +
                ", from: " + node.nodeMsg().toString());
    }
}
