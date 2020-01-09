package name.feinimouse.feinicoinplus.core.node.exception;

import name.feinimouse.feinicoinplus.core.data.NetInfo;
import name.feinimouse.feinicoinplus.core.node.Node;

public class RequestNotSupportException extends BadRequestException {
    public RequestNotSupportException(Node node, NetInfo netInfo, String msg) {
        super("Node(" + nodeMsg(node) + ") received an request(" + netInfo + "), " +
            "but not support for the following reasons: " + msg);
    }
}
