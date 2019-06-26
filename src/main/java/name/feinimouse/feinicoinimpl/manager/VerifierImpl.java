package name.feinimouse.feinicoinimpl.manager;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Verifier;

import java.util.Map;
import java.util.Set;

import lombok.Data;

/**
 * Create by 菲尼莫斯 on 2019/4/15
 * Email: cyhkkha@gmail.com
 * File name: VerifierImpl
 * Program : feinicoin
 * Description :
 */
@Data
public class VerifierImpl implements Verifier, Cloneable {
    // 节点的唯一标示
    private String hash;
    // 保存了一定时间段内所有交易的哈希，确保不会出现重复交易
    private Set<String> transSet;
    // 缓存了所有账户的实时状态
    private Map<String, Account> accounts;
    // 缓存了所有资产的实时状态
    private Map<String, Account> properties;


    @Override
    public int syncBlock(Block b) {
        return 0;
    }

    @Override
    public int verifyTrans(Transaction t) {
        return 0;
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
