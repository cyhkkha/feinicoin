package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.CoverObj;
import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.feinicoinplus.core.PropIgnore;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AdmitPackerArr extends MapSignObj implements HashObj, Cloneable, CoverObj {
    @Getter
    @Setter
    private String hash;
    
    @PropIgnore
    @Setter
    private Packer[] arr;
    
    @PropIgnore
    @Setter
    private Class<?> objClass;

    public AdmitPackerArr(Packer[] arr) {
        this.arr = arr;
    }
    public AdmitPackerArr(Packer[] arr, Class<?> objClass) {
        this(arr);
        this.objClass = objClass;
    }
    public AdmitPackerArr(String hash, Packer[] arr, Class<?> objClass) {
        this(arr, objClass);
        this.hash = hash;
    }

    public AdmitPackerArr copy() {
        try {
            AdmitPackerArr result = (AdmitPackerArr) clone();
            result.setArr(arr.clone());
            Optional.ofNullable(sign).ifPresent(sign -> result.setSign(new ConcurrentHashMap<>(sign)));
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object obj() {
        return arr;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }
}
