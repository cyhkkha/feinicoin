package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.exception.DaoException;
import name.feinimouse.feinicoinplus.exception.TransAdmitFailedException;


public abstract class CenterContext {
    protected final AccountManager accountManager;
    protected final AssetManager assetManager;
    protected final CenterDao centerDao;
    
    public CenterContext(AccountManager accountManager, AssetManager assetManager, CenterDao centerDao) {
        this.accountManager = accountManager;
        this.assetManager = assetManager;
        this.centerDao = centerDao;
    }

    private Packer lastPacker;

    public void commit(Transaction trans) throws TransAdmitFailedException {
        if (!accountManager.commit(trans)) {
            throw new TransAdmitFailedException();
        }
    }

    public void commit(AssetTrans assetTrans) throws TransAdmitFailedException {
        if (!assetManager.commit(assetTrans)) {
            throw new TransAdmitFailedException();
        }
    }

    public void admit(Packer packer) throws DaoException {
        lastPacker = packer;
        centerDao.saveBlock(packer);
    }

    public Block pack(PackerArr transTree, String producer) {
        PackerArr accountTree = accountManager.pack();
        PackerArr assetTree = assetManager.pack();

        Block lastBlock = (Block) lastPacker.obj();

        Block block = new Block(accountTree, assetTree, transTree);
        block.setId(lastBlock.getId());
        block.setPreHash(lastPacker.getHash());
        block.setProducer(producer);
        block.setTimestamp(System.currentTimeMillis());

        return block;
    }
}
