package name.feinimouse.feinicoinplus.core.obj;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class SignObj extends HashObj {
    
    private ConcurrentHashMap<String, String> signMap;
    private HashObj obj;
    
    public SignObj(HashObj obj, String sign) {
        this.obj = obj;
        this.hash = obj.getHash();
        signMap = new ConcurrentHashMap<>();
    }
    
    public void putSign(String signer, String sign) {
        signMap.put(signer, sign);
    }
    
    public String findSign(String signer) {
        return signMap.get(signer);
    }
    
    public void deleteSign(String signer) {
        signMap.remove(signer);
    }

    @Override
    public JSONObject json() {
        return obj.json().put("sign", new JSONObject(signMap));
    }

    @Override
    public String summary() {
        return obj.summary();
    }

    @Override
    public Object getObject() {
        return obj.getObject();
    }
}
