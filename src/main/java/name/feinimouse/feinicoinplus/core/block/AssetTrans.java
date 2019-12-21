package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import org.json.JSONObject;

public class AssetTrans implements BaseObj {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String timestamp;
    @Getter @Setter
    private String operation;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private int number;
    @Getter @Setter
    private Transaction transaction;

    @Override
    public String summary() {
        return json().toString();
    }

    @Override
    public JSONObject json() {
        return new JSONObject(this).put("transaction", transaction.json());
    }
}
