package name.feinimouse.feinicoinplus.sim;

import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;

public interface TransactionGenerator {
    Transaction genRandomTrans();
    AssetTrans genRandomAssetTrans();
    Transaction gen(String sender, String receiver, int coin);
    AssetTrans gen(String address, String sender, String receiver, int number);
    AssetTrans gen(String address, String sender, String receiver, int number, Transaction trans);
    AssetTrans gen(String address, String sender, String receiver, int number, int coin);
}
