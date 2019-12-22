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
    private JSONObject cover;

    public SignAttachObj() {}

    public SignAttachObj(SignObj<T> obj, JSONObject cover) {
        this.obj = obj;
        this.cover = cover;
    }

    @Override
    public JSONObject json() {
        return obj.json().put("cover", cover);
    }
    
}
