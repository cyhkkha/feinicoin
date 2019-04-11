package name.feinimouse.feinicoin.manager;

import java.util.List;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Property;
import name.feinimouse.feinicoin.account.Transcation;
import name.feinimouse.feinicoin.block.Block;

public abstract class Center implements Cloneable {
    // 节点唯一标识
    protected String hash;
    // 是否轮到当前节点记账
    protected boolean isActive;
    // 其他中央节点
    protected List<Center> centerList;
    // 名下的所有排序节点
    protected List<Order> orderList;
    // 最新的区块
    protected Block latestBlock;
    // 出块时间
    public static int blockDelay = 500;

    // 当前缓存的的交易列表
    protected List<Transcation> transList;
    // 当前缓存的的账户列表
    protected List<Account> accountList;
    // 当前缓存的的资产列表
    protected List<Property> properList;

    // 激活当前节点的出块状态
    public abstract int activate();
    // 生产区块
    protected abstract Block createBlock();
    // 将区块写入数据库
    protected abstract int write();
    // 广播生成的区块
    protected abstract int broadcast();
    // 接收并同步一个区块
    public abstract int syncBlock(Block b);

}