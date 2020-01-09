package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;
import name.feinimouse.feinicoinplus.core.data.Transaction;
import name.feinimouse.feinicoinplus.core.node.exception.DaoException;
import name.feinimouse.feinicoinplus.core.node.exception.TransAdmitFailedException;


public interface CenterContext {

    void commit(Transaction trans) throws TransAdmitFailedException;

    void commit(Packer packer) throws TransAdmitFailedException;

    void admit(Packer packer) throws DaoException;

    Block pack(PackerArr transTree, String producer);
}
