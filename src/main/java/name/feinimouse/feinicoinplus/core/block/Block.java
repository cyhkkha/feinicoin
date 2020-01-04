package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;
import name.feinimouse.feinicoinplus.core.PropIgnore;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

import java.util.HashMap;

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
    
    private HashMap<String, String> exFunc;
    
    public Block() {}

    public Block(PackerArr accounts, PackerArr assets, PackerArr transactions) {
        this.accounts = accounts;
        this.assets = assets;
        this.transactions = transactions;
    }

    @Override
    public String genSummary() {
        return BlockObj.genJson(this)
            .put("accounts", accounts.getHash())
            .put("assets", assets.getHash())
            .put("transactions", transactions.getHash())
            .toString();
    }
}
