package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;
import org.json.JSONObject;

@Data
public class NodeMessage implements JsonAble {
    private String sender;
    private String receiver;
    private String network;
    private String callback;
    private int nodeType;
    private int msgType;

    public NodeMessage() {
    }

    public NodeMessage(int nodeType, String network) {
        this.network = network;
        this.nodeType = nodeType;
    }

    public boolean notMatch(int nodeType, int msgType) {
        return getNodeType() != nodeType
            || getMsgType() != msgType;
    }

    @Override
    public JSONObject json() {
        return new JSONObject(this);
    }
}
