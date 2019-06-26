package name.feinimouse.feinicoinimpl.manager;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.manager.Enter;
import name.feinimouse.feinicoin.manager.Order;
import name.feinimouse.feinicoin.manager.Verifier;

import java.util.List;
import java.util.Queue;

import lombok.Data;

/**
 * Create by 菲尼莫斯 on 2019/4/15
 * Email: cyhkkha@gmail.com
 * File name: EntryImpl
 * Program : feinicoin
 * Description :
 */
@Data
public class EntryImpl implements Enter, Cloneable {
    // 节点唯一标识
    private String hash;
    // 上传交易的缓冲池
    private Queue<Transaction> buffer;
    // verifier节点列表，以做缓冲池的校验
    private List<Verifier> verifierList;
    // order节点列表
    private List<Order> orderList;

    @Override
    public int verifier(Transaction t) {
        return 0;
    }

    @Override
    public int addTrans(Transaction t) {
        return 0;
    }

    @Override
    public int commitTrans(Transaction t) {
        return 0;
    }

    @Override
    public String getHash() {
        return this.hash;
    }

    @Override
    public String getName() {
        return null;
    }
}
