package name.feinimouse.feinicoinplus.core.crypt;


import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonFormat;
import org.json.JSONObject;

public class HashObj extends JsonFormat {
    @Getter @Setter
    private String hash;
    @Getter @Setter
    private JsonFormat obj;
    
    public HashObj(JsonFormat t, String hash) {
        this.obj = t;
        this.hash = hash;
    }
    
    public JsonFormat obj() {
        return getObj();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject().put("hash", hash).put("obj", obj.toJson());
    }
}
