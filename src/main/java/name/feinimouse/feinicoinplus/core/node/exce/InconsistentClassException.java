package name.feinimouse.feinicoinplus.core.node.exce;

public class InconsistentClassException extends Exception {
    public InconsistentClassException() {
    }

    public InconsistentClassException(String message) {
        super(message);
    }

    public InconsistentClassException(Class<?> c) {
        super("inconsistent class: " + c);
    }
}
