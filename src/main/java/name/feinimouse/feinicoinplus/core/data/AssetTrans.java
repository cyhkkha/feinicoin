package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

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
