package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.MerkelObj;
import name.feinimouse.feinicoinplus.core.BaseObj;
import org.json.JSONObject;

public class Block implements BaseObj {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private MerkelObj<Account> accounts;
    @Getter @Setter
    private MerkelObj<Asset> assets;
    @Getter @Setter
    private MerkelObj<Transaction> transactions;

    @Getter @Setter
    private String preHash;
    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String producer;

    @Override
    public String summary() {
        return json().toString();
    }

    @Override
    public JSONObject json() {
        return new JSONObject().put("accounts", accounts.gainHash())
            .put("assets", assets.gainHash())
            .put("transactions", transactions.gainHash());
    }
}
