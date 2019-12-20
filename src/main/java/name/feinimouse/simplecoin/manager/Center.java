package name.feinimouse.simplecoin.manager;

import name.feinimouse.simplecoin.block.Block;
import name.feinimouse.simplecoin.block.Hashable;

public interface Center extends Hashable, Nameable {

    // 激活当前节点的出块状态
    void activate();
    // 生产区块
    Block createBlock();
    // 将区块写入数据库
    void write(Block b);
    // 广播生成的区块
    void broadcast();
    // 接收并同步一个区块
    void syncBlock(Block b);

}