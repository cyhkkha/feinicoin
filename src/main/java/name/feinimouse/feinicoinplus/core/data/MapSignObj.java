package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

@Data
public abstract class MapSignObj implements HashSignCover {
    protected Map<String, String> sign;

    private String enter;
    private String order;
    private String verifier;

    public MapSignObj() {}
    
    public MapSignObj(Map<String, String> signMap) {
        sign = signMap;
    }
    
    @Override
    public MapSignObj putSign(String signer, String sign) {
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

    public JSONObject genJson() {
        JSONObject object = new JSONObject().put("hash", getHash());
        Optional.ofNullable(getEnter()).ifPresent(e -> object.put("enter", e));
        Optional.ofNullable(getOrder()).ifPresent(e -> object.put("order", e));
        Optional.ofNullable(getVerifier()).ifPresent(e -> object.put("verifier", e));
        if (!sign.isEmpty()) {
            object.put("sign", sign);
        }
        return object;
    }
}
