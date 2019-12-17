package name.feinimouse.feinicoinplus.core.block;


import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class HashObj extends Jsobj {
    @Getter @Setter
    private String hash;
    @Getter @Setter
    private Jsobj obj;
    
    public HashObj(Jsobj t, String hash) {
        this.obj = t;
        this.hash = hash;
    }
    
    public Jsobj obj() {
        return getObj();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject().put("hash", hash).put("obj", obj.toJson());
    }
}
