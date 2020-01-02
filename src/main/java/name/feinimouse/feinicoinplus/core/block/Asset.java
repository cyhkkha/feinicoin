package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;
import name.feinimouse.feinicoinplus.core.data.AdmitPackerArr;

import java.util.HashMap;

@Data
public class Asset implements JsonAble, Cloneable {
    private String address;
    private String type;
    private String owner;
    private String issuer;
    private int number;
    
    private AdmitPackerArr histories;
    private HashMap<String, String> exFunc;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Asset sub = (Asset) super.clone();
        sub.setHistories(histories.copy());
        sub.setExFunc(new HashMap<>(exFunc));
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
