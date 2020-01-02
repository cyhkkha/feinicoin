package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.HashMap;

@Data
public class Account implements JsonAble {
    private String address;
    private int coin;
    private HashMap<String, String> exFunc;
    
}