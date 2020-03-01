package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;
import name.feinimouse.feinicoinplus.core.data.Transaction;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.core.exception.TransAdmitFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("centerContext")
public class BaseCenContext implements CenterContext {
    Logger logger = LogManager.getLogger(BaseCenContext.class);

    protected AccountManager accountManager;
    protected AssetManager assetManager;

    private Packer lastPacker;

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Autowired
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void commitTrans(Transaction trans) throws TransAdmitFailedException {
        if (!accountManager.commit(trans)) {
            throw new TransAdmitFailedException();
        }
    }

    @Override
    public void commitAssetTrans(Packer packer) throws TransAdmitFailedException {
        if (!assetManager.commit(packer)) {
            throw new TransAdmitFailedException();
        }
    }

    @Override
    public void admit(Packer packer) {
        lastPacker = packer;
    }

    @Override
    public Block pack(PackerArr transTree, String producer) {
        PackerArr accountTree = accountManager.pack();
        PackerArr assetTree = assetManager.pack();

        int id = Optional.ofNullable(lastPacker)
            .map(Packer::obj)
            .map(b -> ((Block) b).getId()).orElse(0);
        
        String hash = Optional.ofNullable(lastPacker)
            .map(Packer::getHash).orElse("0000_0000_0000_0000_0000");

        Block block = new Block(accountTree, assetTree, transTree);
        block.setId(id + 1);
        block.setPreHash(hash);
        block.setProducer(producer);
        block.setTimestamp(System.currentTimeMillis());

        logger.trace("区块打包成功");
        return block;
    }
}
