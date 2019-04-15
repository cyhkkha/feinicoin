package name.feinimouse.feinicoin.manager;

import java.util.List;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Property;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.MerkelTreeNode;

public interface Center extends MerkelTreeNode, Nameable {

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