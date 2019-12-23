package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class BaseSignObj<T> implements SignObj<T> {

    private ConcurrentHashMap<String, String> signMap;
    private HashObj<T> hashObj;
    
    public BaseSignObj(HashObj<T> hashObj) {
        this.hashObj = hashObj;
        signMap = new ConcurrentHashMap<>();
    }

    public BaseSignObj<T> putSign(String signer, String sign) {
        signMap.put(signer, sign);
        return this;
    }
    public String getSign(String signer) {
        return signMap.get(signer);
    }
    public String deleteSign(String signer) {
        return signMap.remove(signer);
    }

    @Override
    public String gainHash() {
        return hashObj.gainHash();
    }

    @Override
    public String summary() {
        return hashObj.summary();
    }

    @Override
    public T obj() {
        return hashObj.obj();
    }

    @Override
    public JSONObject json() {
        return hashObj.json().put("sign", new JSONObject(signMap));
    }
}
