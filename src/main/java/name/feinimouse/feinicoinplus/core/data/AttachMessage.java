package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;
import org.json.JSONObject;

@Data
public class AttachMessage implements JsonAble, Cloneable {
    private String msg;
    private Boolean verifiedResult;
    private String verifier;
    private String order;
    private String enter;
    private String password;

    @Override
    public JSONObject json() {
        return new JSONObject(this);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AttachMessage copy() {
        try {
            return (AttachMessage) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
