package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.HashObj;

@Data
public class MerkelArr implements HashObj {
    private String hash;
    private HashCover[] arr;
    private Class<?> aClass;

    public MerkelArr(String hash, HashCover[] arr, Class<?> aClass) {
        this.hash = hash;
        this.arr = arr;
        this.aClass = aClass;
    }
}
