package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class Carrier {
    
    private NodeMessage nodeMessage;
    private AttachMessage attachMessage;
    private Packer packer;
    private Class<?> fetchClass;

    public Carrier() {}

    public Carrier(NodeMessage nodeMessage, AttachMessage attachMessage) {
        this.nodeMessage = nodeMessage;
        this.attachMessage = attachMessage;
    }
    
    public boolean notMatchAttach(int nodeType, int msgType, Class<?> attachClass) {
        return nodeMessage.notMatch(nodeType, msgType) || !packer.objClass().equals(attachClass);
    }
    
    public boolean notMatchFetch(int nodeType, int msgType, Class<?> fetchClass) {
        return nodeMessage.notMatch(nodeType, msgType) || !getFetchClass().equals(fetchClass);
    }
}
