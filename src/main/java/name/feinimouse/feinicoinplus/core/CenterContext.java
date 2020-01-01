package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.block.Account;
import name.feinimouse.feinicoinplus.core.block.Asset;

public interface CenterContext {
    Account getAccount(String address);
    boolean excludeAccount(String address);
    Account[] getAccounts();
    Asset getAsset(String address, String owner);
    boolean excludeAsset(String address, String owner);
    Asset[] getAssets();
    void putAsset(Asset asset);
}
