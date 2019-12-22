package name.feinimouse.feinicoinplus.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface BaseObj {
    default JSONObject json() {
        return new JSONObject(this);
    }
    static <T extends BaseObj> JSONArray genJson(List<T> list) {
        JSONArray ja = new JSONArray();
        list.forEach(jf -> ja.put(jf.json()));
        return ja;
    }
    static <T extends BaseObj> JSONArray genJson(T[] arr) {
        JSONArray ja = new JSONArray();
        for (BaseObj jf : arr) {
            ja.put(jf.json());
        }
        return ja;
    }
    static <T extends BaseObj> JSONObject genJson(Map<String, T> map) {
        JSONObject json = new JSONObject();
        map.forEach((key, value) -> json.put(key, value.json()));
        return json;
    }
}
