package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinaryJsonObj<T extends JsonAble> implements OrdinaryObj {
    private T obj;
    private String summary;
    
    public OrdinaryJsonObj(T obj, String summary) {
        this.obj = obj;
        this.summary = summary;
    }

    public T obj() {
        return obj;
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
