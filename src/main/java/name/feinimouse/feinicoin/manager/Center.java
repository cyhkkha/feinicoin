package name.feinimouse.feinicoin.manager;

import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.Hashable;

public interface Center extends Hashable, Nameable {

    // 激活当前节点的出块状态
    int activate();
    // 生产区块
    Block createBlock();
    // 将区块写入数据库
    int write();
    // 广播生成的区块
    int broadcast();
    // 接收并同步一个区块
    int syncBlock(Block b);

}