package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;

import java.util.Map;

public class Account implements BaseObj {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int coin;
    @Getter @Setter
    private Map<String, String> exFunc;
    
}