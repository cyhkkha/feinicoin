package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.HashObj;

@Data
public class MerkelObj implements HashObj {
    private String hash;
    private SimpleHashObj[] arr;
    private Class<?> aClass;

    public MerkelObj(String hash, SimpleHashObj[] arr, Class<?> aClass) {
        this.hash = hash;
        this.arr = arr;
        this.aClass = aClass;
    }
}
