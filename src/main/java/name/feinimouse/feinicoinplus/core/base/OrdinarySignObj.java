package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinarySignObj extends SignObj {
    
    private HashObj obj;
    
    public OrdinarySignObj(HashObj obj) {
        super();
        this.obj = obj;
        this.hash = obj.gainHash();
    }
    
    @Override
    public JSONObject json() {
        return obj.json().put("sign", new JSONObject(signMap));
    }

    @Override
    public String summary() {
        return obj.summary();
    }

    @Override
    public Object getObject() {
        return obj.getObject();
    }
}
