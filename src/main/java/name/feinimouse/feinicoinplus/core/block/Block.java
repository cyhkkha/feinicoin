package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;
import name.feinimouse.feinicoinplus.core.SignObj;

import java.util.HashMap;

@Data
public class Block implements JsonAble {
    private int id;
    private SignObj accounts;
    private SignObj assets;
    private SignObj transactions;

    private String preHash;
    private long timestamp;
    private String producer;
    
    private HashMap<String, String> exFunc;
}
