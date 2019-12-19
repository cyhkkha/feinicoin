package name.feinimouse.feinicoinplus.core.base;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SignObj <T> extends HashObj<T>  {

    protected ConcurrentHashMap<String, String> signMap;
    
    public SignObj() {
        signMap = new ConcurrentHashMap<>();
    }

    public SignObj<T> putSign(String signer, String sign) {
        signMap.put(signer, sign);
        return this;
    }
    public String findSign(String signer) {
        return signMap.get(signer);
    }
    public String deleteSign(String signer) {
        return signMap.remove(signer);
    }
}
