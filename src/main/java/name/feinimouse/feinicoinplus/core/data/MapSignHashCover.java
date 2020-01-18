package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public abstract class MapSignHashCover extends MapSignObj implements HashSignObj, HashBlockObj, CoverObj {

    @Getter
    @Setter
    private String enter;
    @Getter
    @Setter
    private String order;
    @Getter
    @Setter
    private String verifier;
    @Getter
    @Setter
    private String center;

    public MapSignHashCover() {
        super();
    }

    public MapSignHashCover(Map<String, String> signMap) {
        super(signMap);
    }

    @Override
    public JSONObject genJson() {
        JSONObject object = new JSONObject().put("hash", getHash());
        Optional.ofNullable(getEnter()).ifPresent(e -> object.put("enter", e));
        Optional.ofNullable(getOrder()).ifPresent(e -> object.put("order", e));
        Optional.ofNullable(getVerifier()).ifPresent(e -> object.put("verifier", e));
        Optional.ofNullable(getCenter()).ifPresent(e -> object.put("center", e));
        if (!signMap.isEmpty()) {
            object.put("sign", signMap);
        }
        return object;
    }
}
