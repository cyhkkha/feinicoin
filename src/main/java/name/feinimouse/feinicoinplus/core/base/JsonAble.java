package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface JsonAble {
    default JSONObject json() {
        return new JSONObject(this);
    }
    static <T extends JsonAble> JSONArray genJson(List<T> list) {
        JSONArray ja = new JSONArray();
        list.forEach(jf -> ja.put(jf.json()));
        return ja;
    }
    static <T extends JsonAble> JSONArray genJson(T[] arr) {
        JSONArray ja = new JSONArray();
        for (JsonAble jf : arr) {
            ja.put(jf.json());
        }
        return ja;
    }
    static <T extends JsonAble> JSONObject genJson(Map<String, T> map) {
        JSONObject json = new JSONObject();
        map.forEach((key, value) -> json.put(key, value.json()));
        return json;
    }
}
