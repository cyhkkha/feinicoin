package name.feinimouse.feinicoinplus.core.manifest;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.obj.JsonObj;

import java.util.Map;

public class Transaction implements JsonObj  {
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
}
