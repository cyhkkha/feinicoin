package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Block implements BlockObj {
    private int id;
    
    @PropIgnore
    private PackerArr accounts;
    @PropIgnore
    private PackerArr assets;
    @PropIgnore
    private PackerArr transactions;

    private String preHash;
    private long timestamp;
    private String producer;
    
    private Map<String, String> exFunc;
    
    public Block() {
        exFunc = new HashMap<>();
    }

    public Block(PackerArr accounts, PackerArr assets, PackerArr transactions) {
        this();
        this.accounts = accounts;
        this.assets = assets;
        this.transactions = transactions;
    }

    @Override
    public String genSummary() {
        return genJson()
            .put("accounts", accounts.getHash())
            .put("assets", assets.getHash())
            .put("transactions", transactions.getHash())
            .toString();
    }
}
