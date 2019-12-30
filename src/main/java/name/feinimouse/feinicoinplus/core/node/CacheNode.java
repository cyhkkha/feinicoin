package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import name.feinimouse.feinicoinplus.core.exception.NodeStopException;
import name.feinimouse.utils.ClassMapContainer;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.utils.OverFlowException;
import name.feinimouse.utils.UnrecognizedClassException;


// 一个提供消息缓存的节点基类
public abstract class CacheNode extends AutoStopNode {

    protected ClassMapContainer<Carrier> cacheWait;

    public CacheNode(int nodeType, ClassMapContainer<Carrier> cacheWait) {
        super(nodeType);
        this.cacheWait = cacheWait;
    }

    public boolean setCacheWaitMax(int max) {
        return cacheWait.setMax(max);
    }


    public void pushContainer(ClassMapContainer<Carrier> container, Carrier carrier) throws BadCommitException {
        try {
            container.put(carrier);
        } catch (UnrecognizedClassException e) {
            e.printStackTrace();
            throw BadCommitException.classNotSupportException(this, carrier.getPacker().objClass());
        } catch (OverFlowException e) {
            e.printStackTrace();
            throw BadCommitException.commitOverflowException(this);
        }
    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadCommitException {
        pushContainer(cacheWait, carrier);
    }


    @Override
    protected void afterWork() {
        cacheWait.clear();
        super.afterWork();
    }

    @Override
    protected void working() throws NodeRunningException, NodeStopException {
        // 处理Cache
        if (cacheWait.size() > 0) {
            resolveCache();
            resetGap();
        }
        // 先执行空窗任务，再判断是否空窗超时
        resolveGapPeriod();
        super.working();
    }

    // 处理Transaction
    protected abstract void resolveCache() throws NodeRunningException, NodeStopException;

    // 处理无工作的空窗期
    protected abstract void resolveGapPeriod() throws NodeRunningException;

}
