package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.utils.JsonUtils;
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
    
    // TODO 使用一个队列
    @Getter @Setter
    private SignObj<AssetTrans>[] histories;
    @Getter @Setter
    private ConcurrentHashMap<String, String> exFunc;
    

    @Override
    public JSONObject json() {
        return new JSONObject(this).put("histories", JsonUtils.genJson(histories));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Asset sub = (Asset) super.clone();
        sub.setHistories(histories.clone());
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
