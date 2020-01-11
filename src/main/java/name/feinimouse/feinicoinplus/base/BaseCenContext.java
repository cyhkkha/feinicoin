package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;
import name.feinimouse.feinicoinplus.core.data.Transaction;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.CenterDao;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.core.node.exception.DaoException;
import name.feinimouse.feinicoinplus.core.node.exception.TransAdmitFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BaseCenContext implements CenterContext {
    Logger logger = LogManager.getLogger(BaseCenContext.class);

    protected AccountManager accountManager;
    protected AssetManager assetManager;
    protected CenterDao centerDao;

    private Packer lastPacker;

    public BaseCenContext(AccountManager accountManager, AssetManager assetManager, CenterDao centerDao) {
        this.accountManager = accountManager;
        this.assetManager = assetManager;
        this.centerDao = centerDao;
    }

    @Override
    public void commit(Transaction trans) throws TransAdmitFailedException {
        if (!accountManager.commit(trans)) {
            throw new TransAdmitFailedException();
        }
    }

    @Override
    public void commit(Packer packer) throws TransAdmitFailedException {
        if (!assetManager.commit(packer)) {
            throw new TransAdmitFailedException();
        }
    }

    @Override
    public void admit(Packer packer) throws DaoException {
        lastPacker = packer;
        centerDao.saveBlock(packer);
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
