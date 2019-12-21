package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SignObj<T> extends HashObj<T> {

    private ConcurrentHashMap<String, String> signMap;
    
    public SignObj(HashObj<T> obj) {
        this(obj.obj(), obj.summary(), obj.gainHash());
    }
    
    public SignObj(T obj, String summary, String hash) {
        super(obj, summary, hash);
        signMap = new ConcurrentHashMap<>();
    }

    @Override
    public JSONObject json() {
        return super.json().put("sign", new JSONObject(signMap));
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
