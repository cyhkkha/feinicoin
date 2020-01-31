package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapSignObj implements SignObj, Cloneable {
    @Getter
    @Setter
    protected Map<String, String> signMap;
    public MapSignObj() {
        signMap = new ConcurrentHashMap<>();
    }

    public MapSignObj(Map<String, String> signMap) {
        this.signMap = signMap;
    }

    @Override
    public SignObj putSign(String signer, String sign) {
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

    @Override
    public MapSignObj clone() {
        try {
            return (MapSignObj) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
