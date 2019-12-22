package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

public interface BaseObj {
    default JSONObject json() {
        return new JSONObject(this);
    }
}
