package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;
import name.feinimouse.feinicoinplus.core.data.Transaction;
import name.feinimouse.feinicoinplus.core.exception.TransAdmitFailedException;


public interface CenterContext {

    void commitTrans(Transaction trans) throws TransAdmitFailedException;

    void commitAssetTrans(Packer packer) throws TransAdmitFailedException;

    void admit(Packer packer);

    Block pack(PackerArr transTree, String producer);
}
