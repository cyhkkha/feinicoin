package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

public class BaseHashObj<T extends BaseObj> implements HashObj<T> {

    private T obj;
    private String summary;
    private String hash;

    public BaseHashObj(T obj, String summary, String hash) {
        this.obj = obj;
        this.summary = summary;
        this.hash = hash;
    }

    @Override
    public JSONObject json() {
        return new JSONObject().put("obj", obj.json()).put("hash", gainHash());
    }

    @Override
    public String gainHash() {
        return hash;
    }

    @Override
    public String summary() {
        return summary;
    }

    @Override
    public T obj() {
        return obj;
    }
}
