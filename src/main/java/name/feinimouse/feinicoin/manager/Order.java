package name.feinimouse.feinicoin.manager;

import java.util.List;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.MerkelTreeNode;

public interface Order extends MerkelTreeNode, Nameable {
    
    // enter提交交易
    int commit(Transaction t);
    // center取出交易
    List<Transaction> takeout(int count);
    // 根据最新的区块快速验证交易
    void verifyOrder();
    // 导出别名
    String getName();

}