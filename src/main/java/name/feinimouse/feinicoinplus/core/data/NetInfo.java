package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class NetInfo {
    private String sender;
    private String receiver;
    private String network;
    private String callback;
    private String nodeType;
    private String msgType;

    public NetInfo() {
    }

    public NetInfo(String nodeType, String network) {
        this.network = network;
        this.nodeType = nodeType;
    }

    public boolean notMatch(String nodeType, String msgType) {
        return !getNodeType().equals(nodeType)
            || !getMsgType().equals(msgType);
    }

    @Override
    public String toString() {
        return String.format("[%s](%s@%s -> %s)}", msgType, nodeType, sender, receiver);
    }

}
