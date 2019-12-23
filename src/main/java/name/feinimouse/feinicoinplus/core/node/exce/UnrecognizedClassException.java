package name.feinimouse.feinicoinplus.core.node.exce;

public class UnrecognizedClassException extends Exception {
    public UnrecognizedClassException() {
        
    }

    public UnrecognizedClassException(String message) {
        super(message);
    }

    public UnrecognizedClassException(Class<?> c) {
        super("unrecognized class: " + c);
    }
}
