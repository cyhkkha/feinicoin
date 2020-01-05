package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.CenterDao;
import name.feinimouse.feinicoinplus.exception.DaoException;
import org.springframework.stereotype.Component;

@Component("centerDao")
public class NullCenterDao implements CenterDao {
    @Override
    public Packer findBlock(int id) {
        return null;
    }

    @Override
    public void saveBlock(Packer packer) {

    }
}
