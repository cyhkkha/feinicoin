package name.feinimouse.feinicoinplus.core.base;

import org.json.JSONObject;

public class MerkelObj <T> extends HashObj<HashObj<T>[]> {
    
    private HashObj<T>[] arr;
    private String[] hashTree;
    
    public MerkelObj(HashObj<T>[] arr, String[] hashTree) {
        this.hash = hashTree[0];
        this.arr = arr;
        this.hashTree = hashTree;
    }
    
    @Override
    public String summary() {
        return JsonAble.genJson(arr).toString();
    }
    
    @Override
    public HashObj<T>[] obj() {
        return arr;
    }

    @Override
    public JSONObject json() {
        return new JSONObject().put("obj", JsonAble.genJson(arr)).put("hash", hash);
    }

    public String[] hashTree() {
        return hashTree;
    }

    public MerkelObj<T> copy() {
        HashObj<T>[] subArr = arr.clone();
        String[] subTree = hashTree.clone();
        return new MerkelObj<>(subArr, subTree);
    }
}
