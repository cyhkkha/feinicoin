package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinarySignObj<T extends BaseObj> extends SignObj<T> {
    
    public OrdinarySignObj(HashObj<T> obj) {
        super(obj.obj(), obj.summary(), obj.gainHash());
    }
    
    public OrdinarySignObj(T obj, String hash) {
        super(obj, obj.summary(), hash);
    }
    
    @Override
    public JSONObject json() {
        return super.json().put("obj", obj.json());
    }

    @Override
    public OrdinarySignObj<T> copy() {
        return new OrdinarySignObj<>(obj, hash);
    }

}
