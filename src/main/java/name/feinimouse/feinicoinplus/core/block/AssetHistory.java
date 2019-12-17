package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;

public class AssetHistory extends Jsobj {
    @Getter @Setter
    private String timestamp;
    @Getter @Setter
    private String operation;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private int number;
    @Getter @Setter
    private String transaction;
    
}
