package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.HashMap;

@Data
public class AdmitPacker<T extends JsonAble> implements JsonAble {
    private String enter;
    private String order;
    private String verifier;
    private HashMap<String, String> signMap;
    private String hash;
    private T obj;
    private Class<T> tClass;

    public AdmitPacker() {
    }

    public AdmitPacker(AttachInfo attachInfo) {
        enter = attachInfo.getEnter();
        order = attachInfo.getOrder();
        verifier = attachInfo.getVerifier();
    }
}
