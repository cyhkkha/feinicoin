package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonAble;
import org.json.JSONObject;

public class AssetTrans implements JsonAble {
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String timestamp;
    @Getter @Setter
    private String operation;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private String operator;
    @Getter @Setter
    private int number;
    @Getter @Setter
    private Transaction transaction;

    @Override
    public JSONObject json() {
        return new JSONObject()
            .put("obj", new JSONObject(this)
                .put("transaction", transaction.json()));
    }
    
}
