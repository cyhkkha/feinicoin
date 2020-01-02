package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;

import java.util.HashMap;

@Data
public class AssetTrans implements BlockObj {
    private String address;
    private String timestamp;
    private String operation;
    private String receiver;
    private String operator;
    private int number;
    private Transaction transaction;
    private HashMap<String, String> exFunc;

}
