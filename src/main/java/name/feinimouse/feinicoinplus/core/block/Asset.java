package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.base.*;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class Asset implements OrdinaryObj, Cloneable {
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
    private MerkelObj<AssetTrans> histories;
    @Getter @Setter
    private ConcurrentHashMap<String, String> exFunc;
    

    @Override
    public JSONObject json() {
        return new JSONObject(this).put("histories", histories.gainHash());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Asset sub = (Asset) super.clone();
        sub.setHistories(histories.copy());
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

    @Override
    public String summary() {
        return json().toString();
    }
}
