package name.feinimouse.feinicoinplus.core.node.exception;

import name.feinimouse.feinicoinplus.core.node.Node;

public class NodeBusyException extends BadRequestException {
    public NodeBusyException(Node node) {
        super("Node(" + nodeMsg(node) + ") is busy now, please try again later");
    }
}
