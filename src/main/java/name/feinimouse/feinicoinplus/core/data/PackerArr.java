package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import name.feinimouse.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class PackerArr extends MapSignObj<BaseObj[]> {
    
    @Getter @Setter
    private String[] hashTree;
    @Getter @Setter
    private String[] summaryArr;
    private Class<?> aClass;

    public PackerArr(BaseObj[] core, Class<?> aClass) {
        super(core);
        this.aClass = aClass;
    }

    public PackerArr(BaseObj[] core, Map<String, String> signMap, Class<?> aClass) {
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
    public BaseObj[] obj() {
        return core;
    }

    @Override
    public Class<?> objClass() {
        return aClass;
    }

    @Override
    public JSONObject json() {
        return new JSONObject()
            .put("obj", JsonUtils.genJson(core))
            .put("hash", gainHash())
            .put("sign", signMap);
    }
}
