package name.feinimouse.feinicoinplus.core.sim;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.Transaction;

public interface TransactionGenerator {
    Packer genRandomTrans();
    Packer genRandomAssetTrans();
    Carrier genCarrier(Packer packer, String receiver);
    Packer genTrans(String sender, String receiver, int coin);
    Packer genAssetTrans(String address, String operator, String receiver, int number);
    Packer genAssetTrans(String address, String operator, String receiver, int number, Transaction trans);
    Packer genAssetTrans(String address, String operator, String receiver, int number, int coin);
}
