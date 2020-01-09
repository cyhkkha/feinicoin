package name.feinimouse.feinicoinplus.core.node.exception;

import name.feinimouse.feinicoinplus.core.node.Node;

public class NodeNotWorkingException extends BadRequestException {
    public NodeNotWorkingException(Node node) {
        super("Node Dose Not Work, Commit Failed from: " + nodeMsg(node));
    }
}
