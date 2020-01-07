package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.data.AssetTrans;
import name.feinimouse.feinicoinplus.core.data.Transaction;
import name.feinimouse.feinicoinplus.core.sim.TransactionGenerator;
import org.springframework.stereotype.Component;

@Component("TransactionGenerator")
public class SimpleTransGen implements TransactionGenerator {
    @Override
    public Transaction genRandomTrans() {
        return null;
    }

    @Override
    public AssetTrans genRandomAssetTrans() {
        return null;
    }

    @Override
    public Transaction gen(String sender, String receiver, int coin) {
        return null;
    }

    @Override
    public AssetTrans gen(String address, String sender, String receiver, int number) {
        return null;
    }

    @Override
    public AssetTrans gen(String address, String sender, String receiver, int number, Transaction trans) {
        return null;
    }

    @Override
    public AssetTrans gen(String address, String sender, String receiver, int number, int coin) {
        return null;
    }
}
