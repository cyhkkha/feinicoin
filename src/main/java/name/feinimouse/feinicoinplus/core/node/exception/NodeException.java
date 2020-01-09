package name.feinimouse.feinicoinplus.core.node.exception;

import name.feinimouse.feinicoinplus.core.node.Node;

public class NodeException extends Exception {

    public static String nodeMsg(Node node) {
        if (node == null) {
            return null;
        }
        return node.getNodeType() + "@" + node.getAddress();
    }
    
    public NodeException() {
    }

    public NodeException(String message) {
        super(message);
    }

    public NodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodeException(Throwable cause) {
        super(cause);
    }
}
