package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.CoverObj;
import name.feinimouse.feinicoinplus.core.HashObj;

public class SimpleHashObj implements HashObj, CoverObj {
    @Setter
    private Class<?> objClass;
    private Object obj;
    @Getter @Setter
    private String hash;
    
    public SimpleHashObj(Object core, String hash) {
        obj = core;
        objClass = core.getClass();
        this.hash = hash;
    }

    @Override
    public Object obj() {
        return obj;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }
}
