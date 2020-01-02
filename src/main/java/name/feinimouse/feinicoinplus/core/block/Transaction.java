package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.HashMap;

@Data
public class Transaction implements JsonAble {
    private String timestamp;
    private String sender;
    private String receiver;
    private int number;
    private HashMap<String, String> exFunc;
}
