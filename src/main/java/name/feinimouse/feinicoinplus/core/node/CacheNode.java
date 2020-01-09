package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.exception.*;
import name.feinimouse.utils.ClassMapContainer;
import name.feinimouse.utils.exception.OverFlowException;
import name.feinimouse.utils.exception.UnrecognizedClassException;


// 一个提供消息缓存的节点基类
public abstract class CacheNode extends AutoStopNode {

    protected ClassMapContainer<Carrier> cacheWait;

    public CacheNode(String nodeType, ClassMapContainer<Carrier> cacheWait) {
        super(nodeType);
        this.cacheWait = cacheWait;
    }

    public boolean setCacheWaitMax(int max) {
        return cacheWait.setMax(max);
    }


    public void pushContainer(ClassMapContainer<Carrier> container, Carrier carrier) throws BadRequestException {
        try {
            container.put(carrier);
        } catch (UnrecognizedClassException e) {
            e.printStackTrace();
            throw new RequestNotSupportException(this, carrier.getNetInfo()
                , "Class not support: " + carrier.getPacker().objClass());
        } catch (OverFlowException e) {
            e.printStackTrace();
            throw new NodeBusyException(this);
        }
    }

    @Override
    protected void requestCheck(Carrier carrier) throws BadRequestException {
        super.requestCheck(carrier);
        // 必须携带packer
        if (carrier.getPacker() == null) {
            throw new RequestNotSupportException(this, carrier.getNetInfo(), "message without packer");
        }
    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadRequestException {
        pushContainer(cacheWait, carrier);
    }


    @Override
    protected void afterWork() {
        cacheWait.clear();
        super.afterWork();
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
