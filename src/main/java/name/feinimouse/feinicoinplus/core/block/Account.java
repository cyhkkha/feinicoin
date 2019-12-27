package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.Map;

public class Account implements JsonAble {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int coin;
    @Getter @Setter
    private Map<String, String> exFunc;
    
}