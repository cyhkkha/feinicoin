package name.feinimouse.feinicoinplus.core.node.exception;

import name.feinimouse.feinicoinplus.core.node.Node;

public class NodeStopException extends NodeRunningException {
    public NodeStopException(Node node, String message) {
        super("Node(" + nodeMsg(node) + ") with the following reason: " + message);
    }
}
