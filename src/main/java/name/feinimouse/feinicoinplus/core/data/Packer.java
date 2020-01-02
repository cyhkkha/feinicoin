package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.CoverObj;
import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.feinicoinplus.core.BlockObj;

import java.util.HashMap;

public class Packer extends MapSignObj implements HashObj, CoverObj {

    @Getter
    @Setter
    private String hash;

    @Setter
    private Class<?> objClass;

    public Packer(BlockObj core) {
        super(core);
        objClass = core.getClass();
    }

    public Packer(Object core, HashMap<String, String> signMap) {
        super(core, signMap);
    }

    @Override
    public Object obj() {
        return core;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }
    
}
