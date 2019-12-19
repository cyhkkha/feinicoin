package name.feinimouse.feinicoinplus.core.manifest;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.obj.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class Asset implements Cloneable, JsonObj {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String type;
    @Getter @Setter
    private String owner;
    @Getter @Setter
    private String issuer;
    @Getter @Setter
    private int number;
    @Getter @Setter
    private LinkedList<AssetHistory> histories;
    @Getter @Setter
    private HashMap<String, String> exFunc;
    

    @Override
    public JSONObject json() {
        return new JSONObject(this).put("histories", JsonObj.genJson(histories));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Asset sub = (Asset) super.clone();
        LinkedList<AssetHistory> h = new LinkedList<>(histories);
        sub.setHistories(h);
        HashMap<String, String> e = new HashMap<>(exFunc);
        sub.setExFunc(e);
        return sub;
    }
    
}
