package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import org.json.JSONObject;

import java.util.Map;

public class Packer extends MapSignObj<BaseObj> {
    
    @Getter @Setter
    private String hash;
    @Getter @Setter
    private String summary;

    public Packer(BaseObj core) {
        super(core);
    }

    public Packer(BaseObj core, Map<String, String> signMap) {
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
    public BaseObj obj() {
        return core;
    }

    @Override
    public Class<?> objClass() {
        return core.getClass();
    }

    @Override
    public JSONObject json() {
        return new JSONObject()
            .put("obj", core.json())
            .put("hash", hash)
            .put("signs", signMap);
    }
}
