package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class NetInfo {
    private String sender;
    private String receiver;
    private String network;
    private String callback;
    private int nodeType;
    private int msgType;

    public NetInfo() {
    }

    public NetInfo(int nodeType, String network) {
        this.network = network;
        this.nodeType = nodeType;
    }

    public boolean notMatch(int nodeType, int msgType) {
        return getNodeType() != nodeType
            || getMsgType() != msgType;
    }
    
}
