package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignCoverObj;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

// Order基类
public abstract class OrderNode extends CacheNode {
    
    // Order保存的验证后的消息的缓存
    protected ConcurrentLinkedQueue<SignObj<Transaction>> transVerified;
    protected ConcurrentLinkedQueue<SignObj<AssetTrans>> assetTransVerified;
    
    // 以下是缓存消息的时间
    @Getter @Setter
    protected int transVerifiedMax = 10;
    @Getter @Setter
    protected int assetTransVerifiedMax = 10;
    
    // 节点类型为Order
    public OrderNode() {
        super("order");
        transVerified = new ConcurrentLinkedQueue<>();
        assetTransVerified = new ConcurrentLinkedQueue<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> SignCoverObj<T> fetch(JSONObject json, Class<T> tClass) throws BadCommitException {
        String origin = json.getString("origin");
        if (!origin.toLowerCase().equals("center")) {
            throw new BadCommitException("Can't resolve origin of " + origin);
        }
        if (tClass.equals(Transaction.class)) {
            return coverSign((SignObj<T>) transVerified.poll());
        }
        if (tClass.equals(AssetTrans.class)) {
            return coverSign((SignObj<T>) assetTransVerified.poll());
        }
        throw new BadCommitException(tClass);
    }

    @Override
    protected void afterWork() {
        super.afterWork();
        transVerified.clear();
        assetTransVerified.clear();
    }
}
