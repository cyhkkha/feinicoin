package name.feinimouse.feinicoinplus.core.block;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public abstract class Jsobj {
    public JSONObject toJson() {
        return new JSONObject(this);
    }
    @Override
    public String toString() {
        return toJson().toString();
    }
    public static <T extends Jsobj> JSONArray genJson(List<T> list) {
        JSONArray ja = new JSONArray();
        list.forEach(jo -> ja.put(jo.toJson()));
        return ja;
    }
}
