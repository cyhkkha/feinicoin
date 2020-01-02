package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

@Data
public class PackerArr<T extends JsonAble> implements JsonAble, Cloneable {
    private String hash;
    private AdmitPacker<T>[] obj;
    
    public PackerArr() {}
    
    public PackerArr(AdmitPacker<T>[] obj, String hash) {
        this.hash = hash;
        this.obj = obj;
    }
    
    public PackerArr<T> copy() {
        try {
            @SuppressWarnings("unchecked")
            PackerArr<T> result = (PackerArr<T>) clone();
            result.setObj(obj.clone());
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
