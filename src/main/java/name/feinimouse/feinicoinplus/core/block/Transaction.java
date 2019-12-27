package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.Map;

public class Transaction implements JsonAble {
    @Getter @Setter
    private String timestamp;
    @Getter @Setter
    private String sender;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private int number;
    @Getter @Setter
    private Map<String, String> exFunc;
}
