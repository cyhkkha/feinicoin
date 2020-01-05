package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.block.*;
import name.feinimouse.feinicoinplus.core.exception.TransAdmitFailedException;

public interface CenterContext {
    Account[] getAccounts();
    Asset[] getAssets();
    void admitTransaction(Transaction transaction) throws TransAdmitFailedException;
    void admitAssetTrans(AssetTrans assetTrans) throws TransAdmitFailedException;
    
    Packer getLastBlock();
    void commitNewBlock(Packer packer);
}
