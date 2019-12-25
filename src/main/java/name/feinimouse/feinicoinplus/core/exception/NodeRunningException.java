package name.feinimouse.feinicoinplus.core.exception;

public class NodeRunningException extends Exception {
    public NodeRunningException() {
    }
    public NodeRunningException(String message) {
        super(message);
    }
    public static NodeRunningException invalidStartException(String msg) {
        return new NodeRunningException("Node Invalid Start Exception: [ " + msg + " ]");
    }
}
