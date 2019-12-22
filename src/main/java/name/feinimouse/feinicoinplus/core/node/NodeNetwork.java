package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.node.exce.NoSuchNodeException;
import org.json.JSONObject;

public interface NodeNetwork {
    String getAddress();
    <T> boolean commit(String address, SignAttachObj<T> attachObj, Class<T> tClass) throws NoSuchNodeException;
    boolean send(String address, JSONObject json) throws NoSuchNodeException;
    <T> SignAttachObj<T> fetch(String address, JSONObject json, Class<T> tClass) throws NoSuchNodeException;
    JSONObject fetch(String address, JSONObject json) throws NoSuchNodeException;
}
