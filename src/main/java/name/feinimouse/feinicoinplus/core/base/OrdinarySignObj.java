package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinarySignObj<T> extends SignObj<T> {
    
    private HashObj<T> obj;
    
    public OrdinarySignObj(HashObj<T> obj) {
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
    public T obj() {
        return obj.obj();
    }
}
