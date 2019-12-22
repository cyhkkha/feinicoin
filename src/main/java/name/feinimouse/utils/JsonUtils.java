package name.feinimouse.utils;

import name.feinimouse.feinicoinplus.core.BaseObj;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    public static <T extends BaseObj> JSONArray genJson(List<T> list) {
        JSONArray ja = new JSONArray();
        list.forEach(jf -> ja.put(jf.json()));
        return ja;
    }
    public static <T extends BaseObj> JSONArray genJson(T[] arr) {
        JSONArray ja = new JSONArray();
        for (BaseObj jf : arr) {
            ja.put(jf.json());
        }
        return ja;
    }
    public static <T extends BaseObj> JSONObject genJson(Map<String, T> map) {
        JSONObject json = new JSONObject();
        map.forEach((key, value) -> json.put(key, value.json()));
        return json;
    }
}
