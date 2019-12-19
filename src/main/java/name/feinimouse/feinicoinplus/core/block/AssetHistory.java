package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.base.JsonAble;

public class AssetHistory implements JsonAble {
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
