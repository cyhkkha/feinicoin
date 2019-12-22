package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.node.exce.NoSuchNodeException;
import org.json.JSONObject;

public abstract class NodeNetwork {
    @Setter @Getter
    protected String address;
    
    public abstract <T> boolean commit(String address, SignAttachObj<T> attachObj, Class<T> tClass) throws NoSuchNodeException;
    public abstract boolean send(String address, JSONObject json) throws NoSuchNodeException;
    public abstract <T> SignAttachObj<T> fetch(String address, JSONObject json, Class<T> tClass) throws NoSuchNodeException;
}
