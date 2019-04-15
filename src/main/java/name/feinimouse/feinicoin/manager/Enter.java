package name.feinimouse.feinicoin.manager;

import java.util.List;
import java.util.Queue;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.MerkelTreeNode;

public interface Enter extends MerkelTreeNode, Nameable {
    // 从verifier验证交易
    int verifier(Transaction t);
    // 向缓冲池中添加交易
    int addTrans(Transaction t);
    // 向order提交验证后的交易
    int commitTrans(Transaction t);
}