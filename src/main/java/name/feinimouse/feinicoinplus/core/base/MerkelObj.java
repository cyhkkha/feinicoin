package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class MerkelObj <T extends BaseObj> extends HashObj<T[]> {
    
    private String[] hashTree;
    
    public MerkelObj(T[] arr, String[] hashTree) {
        super(arr, BaseObj.genJson(arr).toString(), hashTree[0]);
        this.hashTree = hashTree;
    }

    public String[] hashTree() {
        return hashTree.clone();
    }

    @Override
    public JSONObject json() {
        return super.json().put("obj", BaseObj.genJson(obj));
    }

    @Override
    public MerkelObj<T> copy() {
        return new MerkelObj<>(obj.clone(), hashTree());
    }
}
