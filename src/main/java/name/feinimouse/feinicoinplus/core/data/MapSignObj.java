package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.Map;

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
}
