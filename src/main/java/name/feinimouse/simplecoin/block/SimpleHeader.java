package name.feinimouse.simplecoin.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.feinicoin.block.Header;
import name.feinimouse.simplecoin.SimpleSign;
import org.json.JSONObject;

public class SimpleHeader implements Header {
    @Getter @Setter
    private long number;
    @Getter @Setter
    private String preHash;
    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String producer;
    @Getter @Setter
    private String version;
    
    @Getter @Setter
    private String transRoot;
    @Getter @Setter
    private String assetRoot;
    @Getter @Setter
    private String accountRoot;
    
    @Getter
    private String summary;
    
    @Getter @Setter
    private SimpleSign sign;
    
    public SimpleHeader(JSONObject json) {
        number = json.getLong("number");
        preHash = json.getString("preHash");
        timestamp = json.getLong("timestamp");
        producer = json.getString("producer");
        transRoot = json.getString("transRoot");
        accountRoot = json.getString("accountRoot");
        assetRoot = json.getString("assetRoot");
        version = json.getString("version");
        this.summary = json.toString();
    }
}
