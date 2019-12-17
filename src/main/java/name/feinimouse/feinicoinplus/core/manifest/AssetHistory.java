package name.feinimouse.feinicoinplus.core.manifest;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonFormat;

public class AssetHistory extends JsonFormat {
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
