package name.feinimouse.feinicoinplus.core.obj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface JsonObj {
    default JSONObject json() {
        return new JSONObject(this);
    }
    static <T extends JsonObj> JSONArray genJson(List<T> list) {
        JSONArray ja = new JSONArray();
        list.forEach(jf -> ja.put(jf.json()));
        return ja;
    }
    static <T extends JsonObj> JSONArray genJson(T[] arr) {
        JSONArray ja = new JSONArray();
        for (JsonObj jf : arr) {
            ja.put(jf.json());
        }
        return ja;
    }
    static <T extends JsonObj> JSONObject genJson(Map<String, T> map) {
        JSONObject json = new JSONObject();
        map.forEach((key, value) -> json.put(key, value.json()));
        return json;
    }
}
