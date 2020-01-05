package name.feinimouse.feinicoinplus.exception;

public class NoSuchNodeException extends NullPointerException {
    public NoSuchNodeException(String type, String address) {
        super("Can't find Node with type: " + type + " and address: " + address);
    }
}
