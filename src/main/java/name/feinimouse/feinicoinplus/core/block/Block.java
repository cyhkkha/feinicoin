package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;
import name.feinimouse.feinicoinplus.core.data.AdmitPackerArr;
import name.feinimouse.feinicoinplus.core.data.MerkelObj;

import java.util.HashMap;

@Data
public class Block implements BlockObj {
    private int id;
    private MerkelObj accounts;
    private MerkelObj assets;
    private AdmitPackerArr transactions;

    private String preHash;
    private long timestamp;
    private String producer;
    
    private HashMap<String, String> exFunc;
}
