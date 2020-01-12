package name.feinimouse.feinicoinplus.core.exception;

import name.feinimouse.feinicoinplus.core.data.NetInfo;
import name.feinimouse.feinicoinplus.core.node.Node;

public class InvalidRequestException extends BadRequestException {
    public InvalidRequestException(Node node, NetInfo netInfo) {
        super("Node(" + nodeMsg(node) + ") received an invalid request: " + netInfo);
    }
}
