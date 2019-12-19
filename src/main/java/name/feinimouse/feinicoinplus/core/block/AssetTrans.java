package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.base.OrdinaryObj;

public class AssetTrans implements OrdinaryObj {
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

    @Override
    public String summary() {
        return json().toString();
    }
}
