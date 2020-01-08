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
