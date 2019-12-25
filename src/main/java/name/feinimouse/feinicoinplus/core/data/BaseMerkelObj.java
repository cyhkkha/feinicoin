package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.feinicoinplus.core.BaseObj;
import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.utils.JsonUtils;
import org.json.JSONObject;

import java.util.Arrays;

public class BaseMerkelObj<T extends BaseObj> implements HashObj<T[]> {
    private String[] hashTree;
    private T[] obj;
    private String[] summary;

    public BaseMerkelObj(T[] obj, String[] hashTree, String[] summary) {
        this.hashTree = hashTree;
        this.obj = obj;
        this.summary = summary;
    }

    public String[] hashTree() {
        return hashTree.clone();
    }

    @Override
    public JSONObject json() {
        return new JSONObject().put("obj", JsonUtils.genJson(obj))
            .put("hash", gainHash());
    }

    @Override
    public String gainHash() {
        return hashTree[0];
    }

    @Override
    public String summary() {
        return Arrays.toString(summary);
    }

    @Override
    public T[] obj() {
        return obj;
    }
}
