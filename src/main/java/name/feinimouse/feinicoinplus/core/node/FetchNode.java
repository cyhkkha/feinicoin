package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

// Order基类
public abstract class FetchNode extends CacheNode {
    
    // Order保存的验证后的消息的缓存
    protected ConcurrentLinkedQueue<SignObj<Transaction>> transFetch;
    protected ConcurrentLinkedQueue<SignObj<AssetTrans>> assetTransFetch;
    
    // 以下是缓存消息的时间
    @Getter @Setter
    protected int transFetchMax = 10;
    @Getter @Setter
    protected int assetTransFetchMax = 10;
    
    // 节点类型为Order
    public FetchNode(String nodeType) {
        super(nodeType);
        transFetch = new ConcurrentLinkedQueue<>();
        assetTransFetch = new ConcurrentLinkedQueue<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> SignAttachObj<T> fetch(JSONObject json, Class<T> tClass) throws BadCommitException {
        String origin = json.getString("origin");
        if (!origin.toLowerCase().equals("center")) {
            throw new BadCommitException("Can't resolve origin of " + origin);
        }
        if (tClass.equals(Transaction.class)) {
            return coverSign((SignObj<T>) transFetch.poll());
        }
        if (tClass.equals(AssetTrans.class)) {
            return coverSign((SignObj<T>) assetTransFetch.poll());
        }
        throw new BadCommitException(tClass);
    }

    @Override
    protected void afterWork() {
        super.afterWork();
        transFetch.clear();
        assetTransFetch.clear();
    }
}
