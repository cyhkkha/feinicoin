package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;

import java.util.HashMap;

@Data
public class Account implements BlockObj {
    private String address;
    private int coin;
    private HashMap<String, String> exFunc;
    
}