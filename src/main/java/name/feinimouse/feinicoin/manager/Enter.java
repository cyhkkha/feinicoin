package name.feinimouse.feinicoin.manager;

import java.util.List;
import java.util.Queue;

import name.feinimouse.feinicoin.account.Transcation;

public abstract class Enter {
    // 上传交易的缓冲池
    protected Queue<Transcation> buffer;

    // verifier节点列表，以做缓冲池的校验
    protected List<Verifier> verifierList;
    // order节点列表
    protected List<Order> orderList;


    // 从verifier验证交易
    protected abstract int verifier(Transcation t);
    // 向缓冲池中添加交易
    protected abstract int addTrans(Transcation t);
    // 向order提交验证后的交易
    protected abstract int commitTrans(Transcation t);
}