package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Account extends Jsobj {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int coin;
    @Getter @Setter
    private Map<String, String> exFunc;
    
}