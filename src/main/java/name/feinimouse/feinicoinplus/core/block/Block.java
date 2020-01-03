package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

import java.util.HashMap;

@Data
public class Block implements BlockObj {
    private int id;
    private PackerArr accounts;
    private PackerArr assets;
    private PackerArr transactions;

    private String preHash;
    private long timestamp;
    private String producer;
    
    private HashMap<String, String> exFunc;
}
