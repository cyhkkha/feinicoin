package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class Carrier {
    
    private NetInfo netInfo;
    private AttachInfo attachInfo;
    private Packer packer;
    private Class<?> fetchClass;

    public Carrier() {}

    public Carrier(NetInfo netInfo, AttachInfo attachInfo) {
        this.netInfo = netInfo;
        this.attachInfo = attachInfo;
    }
    
    public boolean notMatchAttach(int nodeType, int msgType, Class<?> attachClass) {
        return netInfo.notMatch(nodeType, msgType) || !packer.objClass().equals(attachClass);
    }
    
    public boolean notMatchFetch(int nodeType, int msgType, Class<?> fetchClass) {
        return netInfo.notMatch(nodeType, msgType) || !getFetchClass().equals(fetchClass);
    }
    
}
