package name.feinimouse.feinicoinplus.core.node.exception;

import name.feinimouse.feinicoinplus.core.node.Node;

import java.util.Arrays;

public class UninitializedException extends NodeRunningException {
    public UninitializedException(Node node, String ...args) {
        super(
            "The following parameters of Node(" + nodeMsg(node) + ") are not initialized: " 
                + Arrays.toString(args)
        );
    }
}
