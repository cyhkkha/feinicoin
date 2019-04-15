package name.feinimouse.feinicoin.manager;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.MerkelTreeNode;

public interface Verifier extends MerkelTreeNode, Nameable {
    // 同步最新区块
    public int syncBlock(Block b);
    // 验证交易
    public int verifyTrans(Transaction t);
    
}