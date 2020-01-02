package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.HashMap;

@Data
public class AssetTrans implements JsonAble {
    private String address;
    private String timestamp;
    private String operation;
    private String receiver;
    private String operator;
    private int number;
    private Transaction transaction;
    private HashMap<String, String> exFunc;

}
