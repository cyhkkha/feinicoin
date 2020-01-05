package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.exception.TransAdmitFailedException;

public interface CenterContext {
    Account[] getAccounts();
    Asset[] getAssets();
    void admitTransaction(Transaction transaction) throws TransAdmitFailedException;
    void admitAssetTrans(AssetTrans assetTrans) throws TransAdmitFailedException;
    Packer getLastBlock();
    void commitNewBlock(Packer packer);
}
