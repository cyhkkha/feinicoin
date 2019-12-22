package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

// 一个提供消息缓存的节点基类
public abstract class CacheNode extends Node {
    // 缓存Transaction
    protected ConcurrentLinkedQueue<SignAttachObj<Transaction>> transactionWait;
    // 缓存AssetTransaction
    protected ConcurrentLinkedQueue<SignAttachObj<Transaction>> assetTransWait;
    // 缓存普通消息
    protected ConcurrentLinkedQueue<JSONObject> massageWait;
    
    // 以下是缓存的最大长度
    @Getter @Setter
    protected int transactionWaitMax = 10;
    @Getter @Setter
    protected int assetTransWaitMax = 10;
    @Getter @Setter
    protected int massageWaitMax = 10;

    public CacheNode(String nodeType) {
        super(nodeType);
        transactionWait = new ConcurrentLinkedQueue<>();
        assetTransWait = new ConcurrentLinkedQueue<>();
        massageWait = new ConcurrentLinkedQueue<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> int commit(SignAttachObj<T> attachObj, Class<T> tClass) throws BadCommitException {
        if (tClass.equals(Transaction.class)) {
            return transactionWait.size() < transactionWaitMax 
                && transactionWait.add((SignAttachObj<Transaction>) attachObj)
                ? COMMIT_SUCCESS
                : CACHE_OVERFLOW;
        }
        if (tClass.equals(AssetTrans.class)) {
            return assetTransWait.size() < assetTransWaitMax 
                && assetTransWait.add((SignAttachObj<Transaction>) attachObj)
                ? COMMIT_SUCCESS
                : CACHE_OVERFLOW;
        }
        throw new BadCommitException(tClass);
    }

    @Override
    public synchronized int commit(JSONObject json) throws BadCommitException {
        return massageWait.size() < massageWaitMax
            && massageWait.add(json)
            ? COMMIT_SUCCESS
            : CACHE_OVERFLOW;
    }

    @Override
    protected void afterWork() {
        transactionWait.clear();
        assetTransWait.clear();
        massageWait.clear();
    }

    @Override
    protected void working() {
        // 优先处理普通消息
        if (massageWait.size() > 0) {
            resolveMassage();
            return;
        }
        // 处理Transaction
        if (transactionWait.size() > 0) {
            resolveTransaction();
            return;
        }
        // 处理AssetTransaction
        if (assetTransWait.size() > 0) {
            resolveAssetTrans();
        }
    }

    // 处理普通消息
    protected abstract void resolveMassage();
    // 处理Transaction
    protected abstract void resolveTransaction();
    // 处理AssetTransaction
    protected abstract void resolveAssetTrans();
}
