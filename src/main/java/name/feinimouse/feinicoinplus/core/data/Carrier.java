package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.CoverObj;
import org.json.JSONObject;

@Data
public class Carrier {
    
    private String sender;
    private String receiver;
    private String network;
    private int nodeType;
    private int msgType;
    private JSONObject msg;
    private CoverObj<?> attach;
    private Class<?> attachClass;
    private Class<?> subClass;
    private Class<?> fetchClass;

    public Carrier() {}

    public Carrier(String sender, String network, int nodeType) {
        this.sender = sender;
        this.network = network;
        this.nodeType = nodeType;
    }

    public String gainType() {
        return new JSONObject().put("sender", sender)
            .put("nodeType", nodeType)
            .put("msgType", msgType)
            .put("attachClass", attachClass)
            .put("subAttachClass", subClass)
            .toString();
    }
    
    public boolean notMatch(int nodeType, int msgType) {
        return getNodeType() != nodeType
            || getMsgType() != msgType;
    }
    
    public boolean notMatchAttach(int nodeType, int msgType, Class<?> attachClass) {
        return notMatch(nodeType, msgType) || !getAttachClass().equals(attachClass);
    }

    public boolean notMatchAttach(int nodeType, int msgType, Class<?> attachClass, Class<?> subClass) {
        return notMatchAttach(nodeType, msgType, attachClass) || !getSubClass().equals(subClass);
    }
    
    public boolean notMatchFetch(int nodeType, int msgType, Class<?> fetchClass) {
        return notMatch(nodeType, msgType) || !getFetchClass().equals(fetchClass);
    }
}
