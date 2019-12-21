package name.feinimouse.feinicoinplus.core;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class SignCoverObj<T> implements BaseObj {
    @Getter @Setter
    private SignObj<T> obj;
    @Getter @Setter
    private JSONObject cover;

    public SignCoverObj() {}

    public SignCoverObj(SignObj<T> obj, JSONObject cover) {
        this.obj = obj;
        this.cover = cover;
    }

    @Override
    public JSONObject json() {
        return obj.json().put("cover", cover);
    }

    @Override
    public String summary() {
        return obj.summary();
    }
}
