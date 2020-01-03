package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;
import name.feinimouse.feinicoinplus.core.PropIgnore;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

import java.util.HashMap;

@Data
public class Asset implements BlockObj, Cloneable {
    private String address;
    private String type;
    private String owner;
    private String issuer;
    private int number;
    
    @PropIgnore
    private PackerArr histories;
    
    private HashMap<String, String> exFunc;

    @Override
    public String genSummary() {
        return BlockObj.genJson(this)
            .put("histories", histories.getHash())
            .toString();
    }

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
