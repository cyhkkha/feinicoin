package name.feinimouse.feinicoinplus.exception;

import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.data.NetInfo;

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
    
    public static BadCommitException receiverException(Node node, NetInfo message) {
        return new BadCommitException(
            "Error receiver of [" + message.getReceiver() + "] in Node: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException illegalRequestException(Node node) {
        return new BadCommitException("Receive illegal request in node: " + node.nodeMsg().toString());
    }

    public static BadCommitException commitOverflowException(Node node) {
        return new BadCommitException(
            "Commit Failed, Node cache overflow from: " + node.nodeMsg().toString());
    }

    public static BadCommitException classNotSupportException(Node node, Class<?> aClass) {
        return new BadCommitException("Error operation class type of " + aClass
            +" , operation failed from: " + node.nodeMsg().toString());
    }
    
    public static BadCommitException typeNotSupportException(Node node, NetInfo netInfo) {
        return new BadCommitException(
            "Commit not support: [ msgType: " + netInfo.getMsgType() + ", nodeType: " + netInfo.getNodeType() +
                " ], from: " + node.nodeMsg().toString());
    }
    public static BadCommitException requestNotSupport(Node node, String request) {
        return new BadCommitException("Request of " + request + " not support from node: " + node.nodeMsg().toString());
    }
}
