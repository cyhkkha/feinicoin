package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Map;

public class Packer extends MapSignObj {
    
    @Getter @Setter
    private String hash;
    @Getter @Setter
    private String summary;
    @Getter @Setter
    private JSONObject json;
    @Setter
    private Class<?> objClass;

    public Packer(Object core) {
        super(core);
        objClass = core.getClass();
    }

    public Packer(Object core, Map<String, String> signMap) {
        super(core, signMap);
    }


    @Override
    public String gainHash() {
        return hash;
    }

    @Override
    public String summary() {
        return summary;
    }

    @Override
    public Object obj() {
        return core;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }

    @Override
    public JSONObject json() {
        return json
            .put("hash", hash)
            .put("signs", signMap);
    }
}
