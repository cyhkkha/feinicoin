package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.base.OrdinaryObj;

import java.util.Map;

public class Account implements OrdinaryObj {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int coin;
    @Getter @Setter
    private Map<String, String> exFunc;

    @Override
    public String summary() {
        return json().toString();
    }
}