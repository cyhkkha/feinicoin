package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BaseObj;
import org.json.JSONObject;

@Data
public class NodeMessage implements BaseObj {
    private String sender;
    private String receiver;
    private String network;
    private int nodeType;
    private int msgType;

    public boolean notMatch(int nodeType, int msgType) {
        return getNodeType() != nodeType
            || getMsgType() != msgType;
    }
}
