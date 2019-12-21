package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

public class OrdinaryObj<T extends BaseObj> extends HashObj<T> {

    public OrdinaryObj(T obj, String hash) {
        super(obj, obj.summary(), hash);
    }

    @Override
    public JSONObject json() {
        return super.json().put("obj", obj.json());
    }

    @Override
    public HashObj<T> copy() {
        return new OrdinaryObj<>(obj, hash);
    }
}
