package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class PackerArr extends MapSignObj {
    
    @Getter @Setter
    private String[] hashTree;
    @Getter @Setter
    private String[] summaryArr;
    @Getter @Setter
    private JSONObject json;
    @Setter
    private Class<?> aClass;

    public PackerArr(Object[] core, Class<?> aClass) {
        super(core);
        this.aClass = aClass;
    }

    public PackerArr(Object[] core, Class<?> aClass, Map<String, String> signMap) {
        super(core, signMap);
        this.aClass = aClass;
    }

    @Override
    public String gainHash() {
        return hashTree != null && hashTree.length > 0 ? hashTree[0] : null;
    }

    @Override
    public String summary() {
        if (hashTree == null) {
            return null;
        }
        if (hashTree.length <1) {
            return new JSONArray().toString();
        }
        return new JSONArray(summaryArr).toString();
    }

    @Override
    public Object obj() {
        return core;
    }

    @Override
    public Class<?> objClass() {
        return aClass;
    }

    @Override
    public JSONObject json() {
        return json
            .put("hash", gainHash())
            .put("sign", signMap);
    }
}
