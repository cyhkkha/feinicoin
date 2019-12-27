package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

public interface JsonAble {
    default JSONObject json() {
        return new JSONObject().put("obj", new JSONObject(this));
    }
}
