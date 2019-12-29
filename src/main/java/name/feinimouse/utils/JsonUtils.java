package name.feinimouse.utils;

import name.feinimouse.feinicoinplus.core.JsonAble;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class JsonUtils {
    public static <T extends JsonAble> JSONArray genJson(List<T> list) {
        JSONArray ja = new JSONArray();
        list.forEach(jf -> ja.put(jf.json()));
        return ja;
    }
    public static <T extends JsonAble> JSONArray genJson(T[] arr) {
        JSONArray ja = new JSONArray();
        for (JsonAble jf : arr) {
            ja.put(jf.json());
        }
        return ja;
    }
    public static <T extends JsonAble> JSONObject genJson(Map<String, T> map) {
        JSONObject json = new JSONObject();
        map.forEach((key, value) -> json.put(key, value.json()));
        return json;
    }
    
    public static <T extends JsonAble> JSONArray genJson(Queue<T> queue) {
        JSONArray ja = new JSONArray();
        queue.forEach(json -> ja.put(json.json()));
        return ja;
    }
}
