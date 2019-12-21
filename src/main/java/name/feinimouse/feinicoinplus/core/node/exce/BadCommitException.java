package name.feinimouse.feinicoinplus.core.node.exce;

public class BadCommitException extends RuntimeException {
    public BadCommitException() {
        super();
    }

    public BadCommitException(String message) {
        super(message);
    }
    
    public <T> BadCommitException(Class<T> tClass) {
        super("Can't resolve class: " + tClass.toString());
    }
}
