package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.exception.DaoException;

public interface CenterDao {
    Packer findBlock(int id) throws DaoException;
    void saveBlock(Packer packer) throws DaoException;
}
