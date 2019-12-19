package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.base.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;

public interface Order {
    boolean commit(SignObj<Transaction> signObj);
    boolean commit(SignObj<AssetTrans> assetTrans, SignObj<Transaction> transaction);
}
