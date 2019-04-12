package name.feinimouse.feinicoin.manager;

import java.util.Map;
import java.util.Set;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Transcation;
import name.feinimouse.feinicoin.block.Block;

public abstract class Verifier {
    // 保存了一定时间段内所有交易的哈希，确保不会出现重复交易
    protected Set<String> transSet;
    // 缓存了所有账户的实时状态
    protected Map<String, Account> accounts;
    // 缓存了所有资产的实时状态
    protected Map<String, Account> properties;

    // 同步最新区块
    public abstract int syncBlock(Block b);
    // 验证交易
    public abstract int verifyTrans(Transcation t);
}