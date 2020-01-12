package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.*;
import name.feinimouse.utils.exception.OverFlowException;
import name.feinimouse.utils.exception.UnrecognizedClassException;


// 一个提供消息缓存的节点基类
public abstract class CacheNode extends AutoStopNode {

    protected CarrierAttachCMC cacheWait;

    public CacheNode(String nodeType) {
        super(nodeType);
        this.cacheWait = new CarrierAttachCMC(Transaction.class, AssetTrans.class);
    }

    public boolean setCacheWaitMax(int max) {
        return cacheWait.setMax(max);
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadRequestException {
        super.beforeCommit(carrier);
        // 必须携带packer
        if (carrier.getPacker() == null) {
            throw new RequestNotSupportException(this, carrier.getNetInfo(), "message without packer");
        }
    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadRequestException {
        try {
            cacheWait.put(carrier);
        } catch (UnrecognizedClassException e) {
            e.printStackTrace();
            throw new RequestNotSupportException(this, carrier.getNetInfo()
                , "Class not support: " + carrier.getPacker().objClass());
        } catch (OverFlowException e) {
            throw new NodeBusyException(this);
        }
    }


    @Override
    protected void afterWork() {
        super.afterWork();
        cacheWait.clear();
    }

    @Override
    protected void gapWorking() throws NodeRunningException {
        // 处理Cache
        if (cacheWait.size() > 0) {
            resolveCache();
            resetGap();
        }
    }

    // 处理Transaction
    protected abstract void resolveCache() throws NodeRunningException;

}
