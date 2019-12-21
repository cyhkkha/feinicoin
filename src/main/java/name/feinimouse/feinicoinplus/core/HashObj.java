package name.feinimouse.feinicoinplus.core;


import org.json.JSONObject;

public abstract class HashObj<T> implements BaseObj {
    protected String hash;
    protected String summary;
    protected T obj;

    public HashObj(T obj, String summary, String hash) {
        this.obj = obj;
        this.summary = summary;
        this.hash = hash;
    }
    
    public String gainHash() {
        return hash;
    }

    @Override
    public String summary() {
        return summary;
    }

    public T obj() {
        return obj;
    }
    
    @Override
    public JSONObject json() {
        return new JSONObject().put("obj", new JSONObject(obj))
            .put("hash", hash);
    }
    
    public abstract HashObj<T> copy();
}
