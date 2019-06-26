package name.feinimouse.feinicoinimpl.manager;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Order;

import java.util.List;

import lombok.Data;

/**
 * Create by 菲尼莫斯 on 2019/4/15
 * Email: cyhkkha@gmail.com
 * File name: OrderImpl
 * Program : feinicoin
 * Description :
 */
@Data
public class OrderImpl implements Order, Cloneable {
    // 调度节点的唯一标识(公钥)
    private String hash;
    // 最新的区块信息，以做已排序交易的筛选
    private Block latestBlock;

    // 已排好序的交易
    private List<Transaction> order;


    @Override
    public int commit(Transaction t) {
        return 0;
    }

    @Override
    public List<Transaction> takeout(int count) {
        return null;
    }

    @Override
    public void verifyOrder() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getHash() {
        return this.hash;
    }
}
