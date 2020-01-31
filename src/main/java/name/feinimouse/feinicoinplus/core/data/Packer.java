package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Map;

public class Packer extends MapSignHashCover {

    @Getter
    @Setter
    private String hash;

    @PropIgnore
    @Setter
    private BlockObj obj;
    
    @PropIgnore
    @Setter
    private Class<?> objClass;

    public Packer(BlockObj core) {
        obj = core;
        objClass = core.getClass();
    }

    public Packer(BlockObj core, Map<String, String> signMap) {
        super(signMap);
        obj = core;
        objClass = core.getClass();
    }

    @Override
    public Object obj() {
        return obj;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }

    @Override
    public JSONObject genJson() {
        return super.genJson().put("obj", obj.genJson());
    }

    @Override
    public Packer clone() {
        return (Packer) super.clone();
    }
}
