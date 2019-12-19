package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

import java.util.Map;

public class MultiHashObj <T> extends HashObj<Map<String, HashObj<T>>> {
    private Map<String, HashObj<T>> map;
    private String[] hashTree;
    
    public MultiHashObj(Map<String, HashObj<T>> map, String[] hashTree) {
        this.map = map;
        this.hash = hashTree[0];
        this.hashTree = hashTree;
    }
    
    public HashObj<T> get(String key) {
        return map.get(key);
    }
    
    @Override
    public String summary() {
        return hash;
    }

    @Override
    public Map<String, HashObj<T>> obj() {
        return map;
    }

    @Override
    public JSONObject json() {
        return new JSONObject().put("obj", JsonAble.genJson(map)).put("hash", hash);
    }

    public String[] hashTree() {
        return hashTree;
    }
    
}
