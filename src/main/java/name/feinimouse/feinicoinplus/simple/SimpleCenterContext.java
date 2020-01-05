package name.feinimouse.feinicoinplus.simple;

import name.feinimouse.feinicoinplus.core.CenterContext;
import name.feinimouse.feinicoinplus.core.block.Account;
import name.feinimouse.feinicoinplus.core.block.Asset;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.Packer;
import name.feinimouse.feinicoinplus.sim.AccountManager;

// TODO
public class SimpleCenterContext implements CenterContext {
    
    private AccountManager accountManager;

    @Override
    public Account[] getAccounts() {
        return new Account[0];
    }

    @Override
    public Asset[] getAssets() {
        return new Asset[0];
    }
    

    @Override
    public void admitTransaction(Transaction trans) {
        String sender = trans.getSender();
        String receiver = trans.getReceiver();
        if (accountManager.contain(sender) && accountManager.contain(receiver)) {
            Account senderAcc = accountManager.get(trans.getSender());
            Account receiverAcc = accountManager.get(trans.getReceiver());
            int coin = trans.getNumber();
            senderAcc.setCoin(senderAcc.getCoin() - coin);
            receiverAcc.setCoin(receiverAcc.getCoin() + coin);
        }
    }

    @Override
    public void admitAssetTrans(AssetTrans assetTrans) {

    }

    @Override
    public Packer getLastBlock() {
        return null;
    }

    @Override
    public void commitNewBlock(Packer packer) {

    }
}
