package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class Carrier {
    
    private NodeMessage nodeMessage;
    private AttachMessage attachMessage;
    private Packer attach;
    private Class<?> fetchClass;

    public Carrier() {}

    public Carrier(NodeMessage nodeMessage) {
        this.nodeMessage = nodeMessage;
    }
    
    public boolean notMatchAttach(int nodeType, int msgType, Class<?> attachClass) {
        return nodeMessage.notMatch(nodeType, msgType) || !attach.objClass().equals(attachClass);
    }
    
    public boolean notMatchFetch(int nodeType, int msgType, Class<?> fetchClass) {
        return nodeMessage.notMatch(nodeType, msgType) || !getFetchClass().equals(fetchClass);
    }
}
