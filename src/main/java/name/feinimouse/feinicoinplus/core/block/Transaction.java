package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.base.BaseObj;

import java.util.Map;

public class Transaction implements BaseObj {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String timestamp;
    @Getter @Setter
    private String sender;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private String number;
    @Getter @Setter
    private Map<String, String> exFunc;

    @Override
    public String summary() {
        return json().toString();
    }
}
