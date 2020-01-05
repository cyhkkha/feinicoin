package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.exception.DaoException;

public interface CenterDao {
    Packer findBlock(int id) throws DaoException;
    void saveBlock(Packer packer) throws DaoException;
}
