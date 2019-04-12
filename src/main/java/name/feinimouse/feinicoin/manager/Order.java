package name.feinimouse.feinicoin.manager;

import java.util.List;

import name.feinimouse.feinicoin.account.Transcation;
import name.feinimouse.feinicoin.block.Block;

public abstract class Order {
    // 调度节点的唯一标识(公钥)
    protected String hash;
    // 最新的区块信息，以做已排序交易的筛选
    protected Block latestBlock;

    // 已排好序的交易
    protected List<Transcation> order;

    // enter提交交易
    public abstract int commit(Transcation t);
    // center取出交易
    public abstract List<Transcation> takeout(int count);
    // 根据最新的区块快速验证交易
    protected abstract void verifyOrder();
}