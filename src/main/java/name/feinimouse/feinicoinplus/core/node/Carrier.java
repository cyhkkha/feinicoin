package name.feinimouse.feinicoinplus.core.node;

import lombok.Data;
import org.json.JSONObject;

@Data
public class Carrier {
    
    private String sender;
    private String receiver;
    private String network;
    private int nodeType;
    private int msgType;
    private JSONObject msg;
    private Object attach;
    private Class<?> attachClass;
    private Class<?> attachSubClass;
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
            .put("subAttachClass", attachSubClass)
            .toString();
    }
    
    public boolean notMatch(int nodeTYpe, int msgType, Class<?> attachClass) {
        return !(getNodeType() == nodeTYpe
            && getMsgType() == msgType
            && getAttachClass() == attachClass);
    }
    
}
