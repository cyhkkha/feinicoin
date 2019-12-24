package name.feinimouse.feinicoinplus.core.node.exce;

public class NodeStopException extends Exception{
    public NodeStopException(String message) {
        super(message);
    }

    public NodeStopException() {
        super();
    }
}
