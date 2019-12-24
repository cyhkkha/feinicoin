package name.feinimouse.utils;

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
