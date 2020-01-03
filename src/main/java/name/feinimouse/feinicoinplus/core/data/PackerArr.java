package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.CoverObj;
import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.feinicoinplus.core.PropIgnore;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PackerArr extends MapSignObj implements HashObj, Cloneable, CoverObj {
    @Getter
    @Setter
    private String hash;
    
    @PropIgnore
    @Setter
    private Packer[] arr;
    
    @PropIgnore
    @Setter
    private Class<?> objClass;

    public PackerArr(Packer[] arr) {
        this.arr = arr;
    }
    public PackerArr(Packer[] arr, Class<?> objClass) {
        this(arr);
        this.objClass = objClass;
    }
    public PackerArr(String hash, Packer[] arr, Class<?> objClass) {
        this(arr, objClass);
        this.hash = hash;
    }

    public PackerArr copy() {
        try {
            PackerArr result = (PackerArr) clone();
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
