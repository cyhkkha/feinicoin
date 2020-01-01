package name.feinimouse.feinicoinplus.sim;

import name.feinimouse.feinicoinplus.core.CenterContext;
import name.feinimouse.feinicoinplus.core.block.Account;
import name.feinimouse.feinicoinplus.core.block.Asset;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;

public class MapCenterContext implements CenterContext {
    @Override
    public Account getAccount(String address) {
        return null;
    }

    @Override
    public boolean excludeAccount(String address) {
        return false;
    }

    @Override
    public Account[] getAccounts() {
        return new Account[0];
    }

    @Override
    public Asset getAsset(String address, String owner) {
        return null;
    }

    @Override
    public boolean excludeAsset(String address, String owner) {
        return false;
    }

    @Override
    public Asset[] getAssets() {
        return new Asset[0];
    }

    @Override
    public void putAsset(Asset asset) {

    }

    @Override
    public void admitTransaction(Transaction trans) {
        Account sender = getAccount(trans.getSender());
        Account receiver = getAccount(trans.getReceiver());
        int coin = trans.getNumber();
        sender.setCoin(sender.getCoin() - coin);
        receiver.setCoin(receiver.getCoin() + coin);
    }

    @Override
    public void admitAssetTrans(AssetTrans assetTrans) {
        
    }
}
