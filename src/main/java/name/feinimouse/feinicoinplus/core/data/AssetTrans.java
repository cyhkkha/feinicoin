package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;

@Data
public class AssetTrans implements BlockObj {
    
    private String address;
    private long timestamp;
    private String operation = OPERA_TYPE_DEFAULT;
    private String receiver;
    private String operator;
    private int number;
    private Transaction transaction;
    private HashMap<String, String> exFunc;

    public AssetTrans() {
        timestamp = System.currentTimeMillis();
        exFunc = new HashMap<>();
    }
    
    public AssetTrans(String address, String receiver, String operator, int number) {
        this();
        this.address = address;
        this.receiver = receiver;
        this.operator = operator;
        this.number = number;
    }
    
    public static AssetTrans init(Asset asset) {
        AssetTrans assetTrans = new AssetTrans(
            asset.getAddress(),
            asset.getOwner(),
            asset.getIssuer(),
            asset.getNumber()
        );
        assetTrans.setOperation(OPERA_TYPE_INIT);
        return assetTrans;
    }
}
