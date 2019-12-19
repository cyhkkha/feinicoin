package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.base.MerkelObj;
import name.feinimouse.feinicoinplus.core.base.SignObj;
import org.json.JSONObject;

public class Block extends SignObj {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private MerkelObj accounts;
    @Getter @Setter
    private MerkelObj assets;
    @Getter @Setter
    private MerkelObj transactions;

    @Getter @Setter
    private String preHash;
    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String producer;

    public void setHash(String hash) {
        this.hash = hash;
    }
    
    @Override
    public Object getObject() {
        return super.json().put("accounts", accounts.gainHash())
            .put("assets", assets.gainHash())
            .put("transactions", transactions.gainHash());
    }

    @Override
    public String summary() {
        return super.json().put("accounts", accounts.gainHash())
            .put("assets", assets.gainHash())
            .put("transactions", transactions.gainHash())
            .toString();
    }

    @Override
    public JSONObject json() {
        JSONObject o = super.json().put("accounts", accounts.gainHash())
            .put("assets", assets.gainHash())
            .put("transactions", transactions.gainHash())
            .put("sign", new JSONObject(signMap));
        return new JSONObject().put("obj", o).put("hash", hash);
    }
}
