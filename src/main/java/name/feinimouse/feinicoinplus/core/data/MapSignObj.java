package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.feinicoinplus.core.SignObj;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MapSignObj<T> implements SignObj<T> {

    protected Map<String, String> signMap;
    protected T core;

    public MapSignObj(T core) {
        this.core = core;
        signMap = new ConcurrentHashMap<>();
    }

    public MapSignObj(T core, Map<String, String> signMap) {
        this.core = core;
        this.signMap = signMap;
    }
    
    @Override
    public SignObj<T> putSign(String signer, String sign) {
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
}
