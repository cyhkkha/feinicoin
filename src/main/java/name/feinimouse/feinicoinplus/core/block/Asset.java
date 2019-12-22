package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class Asset implements BaseObj, Cloneable {
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
    private AssetTrans[] histories;
    @Getter @Setter
    private ConcurrentHashMap<String, String> exFunc;
    

    @Override
    public JSONObject json() {
        return new JSONObject(this).put("histories", BaseObj.genJson(histories));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Asset sub = (Asset) super.clone();
        AssetTrans[] sunHistory = new AssetTrans[histories.length];
        for (int i = 0; i < histories.length; i++) {
            sunHistory[i] = histories[i].copy();
        }
        sub.setHistories(sunHistory);
        sub.setExFunc(new ConcurrentHashMap<>(exFunc));
        return sub;
    }

    public Asset copy() {
        try {
            return (Asset) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
