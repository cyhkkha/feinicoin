package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.SignObj;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MapSignObj implements SignObj {
    @Getter
    protected Map<String, String> signMap;
    protected Object core;

    public MapSignObj(Object core) {
        this.core = core;
        signMap = new ConcurrentHashMap<>();
    }

    public MapSignObj(Object core, Map<String, String> signMap) {
        this.core = core;
        this.signMap = signMap;
    }

    public MapSignObj putSign(String signer, String sign) {
        signMap.put(signer, sign);
        return this;
    }

    @Override
    public String getSign(String signer) {
        return signMap.get(signer);
    }

    @Override
    public String deleteSign(String signer) {
        return signMap.remove(signer);
    }

    @Override
    public int signSize() {
        return signMap.size();
    }

    @Override
    public boolean excludeSign(String signer) {
        return !signMap.containsKey(signer);
    }
}
