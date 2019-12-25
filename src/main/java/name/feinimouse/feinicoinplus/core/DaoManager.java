package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.block.Block;

public interface DaoManager {
    Block findBlock(int id);
    void saveBlock(Block block);
}
