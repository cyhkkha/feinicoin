package name.feinimouse.simplecoin.manager;

import java.util.List;

import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.block.Hashable;

public interface Order extends Hashable, Nameable {
    
    // enter提交交易
    int commit(Transaction t);
    // center取出交易
    List<Transaction> takeout(int count);
    // 根据最新的区块快速验证交易
    void verifyOrder();
    // 导出别名
    String getName();

}