package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.HashMap;
import java.util.Optional;

@Data
public class AdmitPackerArr<T extends JsonAble> implements JsonAble {
    private String enter;
    private String order;
    private String verifier;
    private HashMap<String, String> signMap;
    private String hash;
    private PackerArr<T> packerArr;

    public String getHash() {
        return Optional.ofNullable(hash).orElse(packerArr.getHash());
    }
}
