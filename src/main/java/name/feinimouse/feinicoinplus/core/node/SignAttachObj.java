package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import name.feinimouse.feinicoinplus.core.SignObj;
import org.json.JSONObject;

public class SignAttachObj<T> implements BaseObj {
    @Getter @Setter
    private SignObj<T> obj;
    @Getter @Setter
    private JSONObject attach;

    public SignAttachObj() {}

    public SignAttachObj(SignObj<T> obj, JSONObject attach) {
        this.obj = obj;
        this.attach = attach;
    }

    @Override
    public JSONObject json() {
        return obj.json().put("attach", attach);
    }
    
}
