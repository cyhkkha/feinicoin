package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

@Data
public class AdmitPackerArr implements JsonAble, Cloneable {
    private String hash;
    private AdmitPacker[] arr;
    private Class<?> objClass;
    
    public AdmitPackerArr() {}

    public AdmitPackerArr(String hash, AdmitPacker[] arr, Class<?> objClass) {
        this.hash = hash;
        this.arr = arr;
        this.objClass = objClass;
    }

    public AdmitPackerArr copy() {
        try {
            AdmitPackerArr result = (AdmitPackerArr) clone();
            result.setArr(arr.clone());
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
