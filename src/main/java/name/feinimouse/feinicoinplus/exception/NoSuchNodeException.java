package name.feinimouse.feinicoinplus.exception;

public class NoSuchNodeException extends NullPointerException {
    public NoSuchNodeException(String address) {
        super("Can't find Node with address of: " + address);
    }
}
