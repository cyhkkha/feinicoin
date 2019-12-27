package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BaseObj;
import name.feinimouse.feinicoinplus.core.SignObj;
import org.json.JSONObject;

public class Block implements BaseObj {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private SignObj<Account[]> accounts;
    @Getter @Setter
    private SignObj<Asset[]> assets;
    @Getter @Setter
    private SignObj<Transaction[]> transactions;

    @Getter @Setter
    private String preHash;
    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String producer;

    @Override
    public JSONObject json() {
        return new JSONObject(this).put("accounts", accounts.json())
            .put("assets", assets.json())
            .put("transactions", transactions.json());
    }
}
