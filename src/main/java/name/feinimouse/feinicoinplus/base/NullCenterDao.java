package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.CenterDao;

public class NullCenterDao implements CenterDao {
    @Override
    public Packer findBlock(int id) {
        return null;
    }

    @Override
    public void saveBlock(Packer packer) {

    }
}
