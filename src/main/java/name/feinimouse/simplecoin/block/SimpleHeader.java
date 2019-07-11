package name.feinimouse.simplecoin.block;

import lombok.Data;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.feinicoin.block.Header;

@Data
public class SimpleHeader implements Header {
    private long number;
    private String preHash;
    private long timestamp;
    private String producer;
    private Sign sign;
    private String transRoot;
    private String assetRoot;
    private String accountRoot;
    private String version;
}
