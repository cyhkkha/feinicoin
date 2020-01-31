package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;

@Data
public class Asset implements BlockObj, Cloneable {
    
    private String address;
    private String type = DEFAULT_TYPE;
    private String owner;
    private String issuer = DEFAULT_ISSUER;
    private int number;
    
    @PropIgnore
    private PackerArr histories;
    
    private HashMap<String, String> exFunc;

    public Asset() {
        exFunc = new HashMap<>();
    }

    public Asset(String address, String owner, int number) {
        this();
        this.address = address;
        this.owner = owner;
        this.number = number;
    }

    @Override
    public String genSummary() {
        return BlockObj.genJson(this)
            .put("histories", histories.getHash())
            .toString();
    }

    @Override
    public Asset clone() {
        Asset sub;
        try {
            sub = (Asset) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        if (histories != null) {
            sub.setHistories(histories.clone());
        }
        sub.setExFunc(new HashMap<>(exFunc));
        return sub;
    }
}
