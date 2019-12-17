package name.feinimouse.feinicoinplus.core.block;

import org.json.JSONObject;

public abstract class Jsobj {
    public abstract JSONObject toJson();
    @Override
    public String toString() {
        return toJson().toString();
    }
}
