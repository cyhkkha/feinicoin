package name.feinimouse.feinicoinplus.core;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class SignCoverObj<T> implements BaseObj {
    @Getter @Setter
    private BaseSignObj<T> obj;
    @Getter @Setter
    private JSONObject cover;

    public SignCoverObj() {}

    public SignCoverObj(BaseSignObj<T> obj, JSONObject cover) {
        this.obj = obj;
        this.cover = cover;
    }

    @Override
    public JSONObject json() {
        return obj.json().put("cover", cover);
    }
    
}
