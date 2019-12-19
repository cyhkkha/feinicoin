package name.feinimouse.feinicoinplus.core.obj;

import org.json.JSONObject;

public class OrdinaryJsonObj implements OrdinaryObj {
    private JsonObj obj;
    private String summary;
    
    public OrdinaryJsonObj(JsonObj obj, String summary) {
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
