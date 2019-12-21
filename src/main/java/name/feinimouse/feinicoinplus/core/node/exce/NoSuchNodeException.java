package name.feinimouse.feinicoinplus.core.node.exce;

public class NoSuchNodeException extends NullPointerException {
    public NoSuchNodeException(String type, String address) {
        super("Can't find Node with type: " + type + " and address: " + address);
    }
}
