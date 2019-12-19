package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class OrdinaryHashObj extends HashObj {
    protected OrdinaryObj obj;

    public OrdinaryHashObj(OrdinaryObj obj, String hash) {
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
    public Object getObject() {
        return obj;
    }
}
