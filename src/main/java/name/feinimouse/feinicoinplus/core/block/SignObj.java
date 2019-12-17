package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class SignObj extends HashObj {
    @Getter @Setter
    private String sign;
    
    public SignObj(HashObj hashObj, String sign) {
        super(hashObj.obj(), hashObj.getHash());
        this.sign = sign;
    }

    @Override
    public JSONObject toJson() {
        return super.toJson().put("sign", sign);
    }
}
