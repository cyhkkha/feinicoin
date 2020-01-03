package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.*;

import java.util.Map;

public class Packer extends MapSignObj implements HashObj, CoverObj {

    @Getter
    @Setter
    private String hash;

    @PropIgnore
    @Setter
    private BlockObj obj;
    
    @PropIgnore
    @Setter
    private Class<?> objClass;

    public Packer(BlockObj core) {
        obj = core;
        objClass = core.getClass();
    }

    public Packer(BlockObj core, Map<String, String> signMap) {
        super(signMap);
        obj = core;
        objClass = core.getClass();
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
