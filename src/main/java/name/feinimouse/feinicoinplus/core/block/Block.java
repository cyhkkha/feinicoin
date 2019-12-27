package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.JsonAble;
import name.feinimouse.feinicoinplus.core.SignObj;
import org.json.JSONObject;

public class Block implements JsonAble {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private SignObj accounts;
    @Getter @Setter
    private SignObj assets;
    @Getter @Setter
    private SignObj transactions;

    @Getter @Setter
    private String preHash;
    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String producer;

    @Override
    public JSONObject json() {
        return new JSONObject()
            .put("obj", new JSONObject(this)
                .put("accounts", accounts.json())
                .put("assets", assets.json())
                .put("transactions", transactions.json()));
    }
}
