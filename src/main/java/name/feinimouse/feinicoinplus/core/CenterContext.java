package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.block.*;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.exception.TransAdmitFailedException;

public interface CenterContext {
    Account getAccount(String address);
    boolean excludeAccount(String address);
    Account[] getAccounts();
    Asset getAsset(String address, String owner);
    boolean excludeAsset(String address, String owner);
    Asset[] getAssets();
    void putAsset(Asset asset) throws TransAdmitFailedException;
    void admitTransaction(Transaction transaction) throws TransAdmitFailedException;
    void admitAssetTrans(AssetTrans assetTrans) throws TransAdmitFailedException;
    
    Packer getLastBlock();
    void commitNewBlock(Packer packer);
}
