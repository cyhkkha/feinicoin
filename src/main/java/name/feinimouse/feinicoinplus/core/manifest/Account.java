package name.feinimouse.feinicoinplus.core.manifest;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonFormat;

import java.util.Map;

public class Account extends JsonFormat {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int coin;
    @Getter @Setter
    private Map<String, String> exFunc;
    
}