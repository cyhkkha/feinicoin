package name.feinimouse.simplecoin.block;

import lombok.Data;
import name.feinimouse.feinicoin.block.Header;
import name.feinimouse.simplecoin.SimpleSign;
import org.json.JSONObject;

@Data
public class SimpleHeader implements Header {
    private String hash;
    private long number;
    private String preHash;
    private long timestamp;
    private String producer;
    private String version;
    
    private String transRoot;
    private String assetRoot;
    private String accountRoot;
    
    private SimpleSign sign;
    
    public JSONObject toJson() {
        return new JSONObject()
            .put("number", number)
            .put("preHash", preHash)
            .put("timestamp", timestamp)
            .put("producer", producer)
            .put("version" , version)
            .put("transRoot", transRoot)
            .put("assetRoot", assetRoot)
            .put("accountRoot", accountRoot);
    }
    
}
