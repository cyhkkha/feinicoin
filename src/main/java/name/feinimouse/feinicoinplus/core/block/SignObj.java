package name.feinimouse.feinicoinplus.core.block;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class SignObj extends HashObj{
    
    private ConcurrentHashMap<String, String> signMap;
    
    public SignObj(HashObj hashObj, String sign) {
        super(hashObj.obj(), hashObj.getHash());
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
    public JSONObject toJson() {
        return super.toJson().put("sign", new JSONObject(signMap));
    }
}
