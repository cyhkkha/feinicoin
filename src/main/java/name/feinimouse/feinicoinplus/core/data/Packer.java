package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.*;

import java.util.Map;

@Data
public class Packer implements SignObj, HashObj, CoverObj, BlockObj {
    
    private String hash;
    protected Map<String, String> sign;

    private String enter;
    private String order;
    private String verifier;

    @PropIgnore
    private Class<?> objClass;
    @PropIgnore
    private BlockObj obj;

    public Packer(BlockObj core) {
        obj = core;
        objClass = core.getClass();
    }

    public Packer(BlockObj core, Map<String, String> signMap) {
        this(core);
        sign = signMap;
    }

    @Override
    public Object obj() {
        return obj;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }

    @Override
    public Packer putSign(String signer, String sign) {
        this.sign.put(signer, sign);
        return this;
    }

    @Override
    public String getSign(String signer) {
        return sign.get(signer);
    }

    @Override
    public String deleteSign(String signer) {
        return sign.remove(signer);
    }

    @Override
    public int signSize() {
        return sign.size();
    }

    @Override
    public boolean excludeSign(String signer) {
        return !sign.containsKey(signer);
    }
    
}
