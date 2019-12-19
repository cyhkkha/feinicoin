package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.obj.HashObj;
import name.feinimouse.feinicoinplus.core.obj.MerkelObj;
import org.json.JSONObject;

public class Block extends HashObj {
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
        JSONObject o = super.json().put("accounts", accounts.getHash())
            .put("assets", assets.getHash())
            .put("transactions", transactions.getHash());
        o.remove("hash");
        return o;
    }

    @Override
    public String summary() {
        JSONObject o = super.json().put("accounts", accounts.getHash())
            .put("assets", assets.getHash())
            .put("transactions", transactions.getHash());
        o.remove("hash");
        return o.toString();
    }

    @Override
    public JSONObject json() {
        JSONObject o = super.json().put("accounts", accounts.getHash())
            .put("assets", assets.getHash())
            .put("transactions", transactions.getHash());
        return new JSONObject().put("obj", o).put("hash", hash);
    }
}
