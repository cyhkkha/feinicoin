package name.feinimouse.feinicoinplus.core.sim;

import name.feinimouse.feinicoinplus.core.data.AssetTrans;
import name.feinimouse.feinicoinplus.core.data.Transaction;

public interface TransactionGenerator {
    Transaction genRandomTrans();
    AssetTrans genRandomAssetTrans();
    Transaction gen(String sender, String receiver, int coin);
    AssetTrans gen(String address, String sender, String receiver, int number);
    AssetTrans gen(String address, String sender, String receiver, int number, Transaction trans);
    AssetTrans gen(String address, String sender, String receiver, int number, int coin);
}
