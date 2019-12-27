package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;
import org.json.JSONObject;

@Data
public class AttachMessage implements JsonAble {
    private String msg;
    private boolean verifiedResult;
    private String verifier;
    private String order;
    private String enter;
    private String password;

    @Override
    public JSONObject json() {
        return new JSONObject(this);
    }
}
