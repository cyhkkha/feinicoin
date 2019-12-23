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
    protected ConcurrentLinkedQueue<SignAttachObj<AssetTrans>> assetTransWait;
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
    public synchronized <T> int commit(SignAttachObj<T> attachObj, Class<T> tClass) {
        if (tClass.equals(Transaction.class)) {
            return transactionWait.size() < transactionWaitMax 
                && transactionWait.add((SignAttachObj<Transaction>) attachObj)
                ? COMMIT_SUCCESS
                : CACHE_OVERFLOW;
        }
        if (tClass.equals(AssetTrans.class)) {
            return assetTransWait.size() < assetTransWaitMax 
                && assetTransWait.add((SignAttachObj<AssetTrans>) attachObj)
                ? COMMIT_SUCCESS
                : CACHE_OVERFLOW;
        }
        return CLASS_UNRECOGNIZED;
    }

    @Override
    public synchronized int commit(JSONObject json) {
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
    protected boolean working() {
        // 优先处理普通消息
        if (massageWait.size() > 0) {
            return resolveMassage(massageWait.poll());
        }
        // 处理Transaction
        if (transactionWait.size() > 0) {
            return resolveTransaction(transactionWait.poll());
        }
        // 处理AssetTransaction
        if (assetTransWait.size() > 0) {
            return resolveAssetTrans(assetTransWait.poll());
        }
        return resolveGapPeriod();
    }

    // 处理普通消息，返回值预示着是否继续运行节点
    protected abstract boolean resolveMassage(JSONObject json);
    // 处理Transaction
    protected abstract boolean resolveTransaction(SignAttachObj<Transaction> signAttachObj);
    // 处理AssetTransaction
    protected abstract boolean resolveAssetTrans(SignAttachObj<AssetTrans> signAttachObj);
    // 处理无工作的空窗期
    protected abstract boolean resolveGapPeriod();
}
