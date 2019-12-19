package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinaryJsonObj implements OrdinaryObj {
    private JsonAble obj;
    private String summary;
    
    public OrdinaryJsonObj(JsonAble obj, String summary) {
        this.obj = obj;
        this.summary = summary;
    }
    
    @Override
    public String summary() {
        return summary;
    }

    @Override
    public JSONObject json() {
        return obj.json();
    }
}
