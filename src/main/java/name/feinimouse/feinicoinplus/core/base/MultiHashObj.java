package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

import java.util.Map;

public class MultiHashObj extends HashObj {
    private Map<String, HashObj> map;
    private String[] hashTree;
    
    public MultiHashObj(Map<String, HashObj> map, String[] hashTree) {
        this.map = map;
        this.hash = hashTree[0];
        this.hashTree = hashTree;
    }
    
    public HashObj get(String key) {
        return map.get(key);
    }
    
    @Override
    public String summary() {
        return hash;
    }

    @Override
    public Map<String, HashObj> getObject() {
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
