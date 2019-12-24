package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import name.feinimouse.utils.ClassMapContainer;

// Order基类
public class Order extends CacheNode {
    
    private ClassMapContainer<Carrier> fetchWait;
    
    public Order(String nodeType) {
        super(NODE_ORDER, new CarrierSubCMC(new Class[]{ Transaction.class, AssetTrans.class }));
        fetchWait = new CarrierSubCMC(new Class[]{ Transaction.class, AssetTrans.class });
//        supportAttachClass = new Class[] { SignObj.class };
//        supportCommitType = new int[] { MSG_COMMIT_ORDER };
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        if (carrier.notMatch(NODE_CENTER, MSG_FETCH_ORDER, SignObj.class)) {
            throw BadCommitException.methodNotSupportException(carrier, this);
        }
        return fetchWait.get(carrier.getFetchClass());
    }
    
    @Override
    protected void beforeCache(Carrier carrier) throws BadCommitException {
        if (carrier.notMatch(NODE_ENTER, MSG_COMMIT_ORDER, SignObj.class)
            && carrier.notMatch(NODE_VERIFIER, MSG_VERIFIER_CALLBACK, SignObj.class)) {
            throw BadCommitException.methodNotSupportException(carrier, this);
        }
        
    }

    @Override
    protected void afterWork() {
        fetchWait.clear();
        super.afterWork();
    }
    

    @Override
    protected void resolveCache() {
        
    }

    @Override
    protected void resolveGapPeriod() {
    }
}
