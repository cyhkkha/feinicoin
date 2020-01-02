package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;

import java.util.HashMap;

@Data
public class Transaction implements BlockObj {
    private String timestamp;
    private String sender;
    private String receiver;
    private int number;
    private HashMap<String, String> exFunc;
}
