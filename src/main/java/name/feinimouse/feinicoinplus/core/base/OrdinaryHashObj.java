package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinaryHashObj <T extends OrdinaryObj> extends HashObj<T> {
    protected T obj;

    public OrdinaryHashObj(T obj, String hash) {
        this.obj = obj;
        this.hash = hash;
    }

    @Override
    public JSONObject json() {
        return new JSONObject().put("obj", obj.json()).put("hash", hash);
    }

    @Override
    public String summary() {
        return obj.summary();
    }

    @Override
    public T obj() {
        return obj;
    }
}
