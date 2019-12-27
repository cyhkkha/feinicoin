package name.feinimouse.feinicoinplus.core.exception;

import java.util.Arrays;

public class NodeRunningException extends Exception {
    public NodeRunningException() {
    }
    public NodeRunningException(String message) {
        super(message);
    }
    public static NodeRunningException invalidStartException(String msg) {
        return new NodeRunningException("Node Invalid Start Exception: [ " + msg + " ]");
    }
    public static NodeRunningException uninitializedException(String... args) {
        return new NodeRunningException(
            "The following parameters are not initialized: " + Arrays.toString(args)
        );
    }
}
