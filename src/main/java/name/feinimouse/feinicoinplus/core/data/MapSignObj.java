package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;
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
            MapSignObj mapSignObj = (MapSignObj) super.clone();
            Optional.ofNullable(signMap).ifPresent(sign -> mapSignObj.signMap = new ConcurrentHashMap<>(sign));
            // map的克隆
//        Optional.ofNullable(signMap).ifPresent(sign -> {
//            Class<?> mapClass = sign.getClass();
//            Map<String, String> map = null;
//            try { // 使用原有的map类型来克隆
//                Constructor<?> con = mapClass.getConstructor(Map.class);
//                if (con != null) {
//                    //noinspection unchecked
//                    map = (Map<String, String>) con.newInstance(sign);
//                    result.setSignMap(map);
//                }
//            } catch (ClassCastException | NoSuchMethodException
//                | IllegalAccessException | InstantiationException
//                | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//            if (map == null) {
//                map = new ConcurrentHashMap<>(sign);
//            }
//            mapSignObj.setSignMap(map);
//        });
            return mapSignObj;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
