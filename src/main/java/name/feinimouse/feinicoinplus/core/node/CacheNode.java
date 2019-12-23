package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.node.exce.InconsistentClassException;
import name.feinimouse.feinicoinplus.core.node.exce.NodeRunningException;
import name.feinimouse.feinicoinplus.core.node.exce.OverFlowException;
import name.feinimouse.feinicoinplus.core.node.exce.UnrecognizedClassException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

// 一个提供消息缓存的节点基类
public abstract class CacheNode extends AutoStopNode {
    
    protected SignAttachCMContainer cacheWait;
    
    // 缓存普通消息
    protected ConcurrentLinkedQueue<JSONObject> massageWait;
    
    @Getter
    protected int massageWaitMax = 10;
    
    public CacheNode(String nodeType, Class<?>[] cacheSupport) {
        super(nodeType);
        cacheWait = new SignAttachCMContainer(cacheSupport);
        massageWait = new ConcurrentLinkedQueue<>();
    }

    public boolean setCacheWaitMax(int max) {
        return cacheWait.setMax(max);
    }
    
    @Override
    public <T> int commit(SignAttachObj<T> attachObj, Class<T> tClass) { 
        int before = beforeCache(attachObj.getAttach());
        if (before != 0) {
            return before;
        }
        try {
            cacheWait.put(tClass, attachObj);
            return COMMIT_SUCCESS;
        } catch (UnrecognizedClassException e) {
            e.printStackTrace();
            return CLASS_UNRECOGNIZED;
        } catch (InconsistentClassException e) {
            e.printStackTrace();
            return CLASS_INCONSISTENT;
        } catch (OverFlowException e) {
            e.printStackTrace();
            return CACHE_OVERFLOW;
        }
    }
    
    // 检查附加信息
    protected int beforeCache(JSONObject attach) {
        if (isStop()) {
            return NODE_NOT_WORKING;
        }
        return 0;
    }

    @Override
    public int commit(JSONObject json) {
        if (isStop()) {
            return NODE_NOT_WORKING;
        }
        return massageWait.size() < massageWaitMax
            && massageWait.add(json)
            ? COMMIT_SUCCESS
            : CACHE_OVERFLOW;
    }

    public synchronized void setMassageWaitMax(int massageWaitMax) {
        this.massageWaitMax = massageWaitMax;
    }

    @Override
    protected void afterWork() {
        cacheWait.clear();
        massageWait.clear();
        super.afterWork();
    }

    @Override
    protected boolean working() throws NodeRunningException {
        // 优先处理普通消息
        if (massageWait.size() > 0) {
            boolean res = resolveMassage(massageWait.poll());
            resetGap();
            return res;
        }
        // 处理Cache
        if (cacheWait.size() > 0) {
            boolean res = resolveWait();
            resetGap();
            return res;
        }
        // 先执行空窗任务，再判断是否空窗超时
        return resolveGapPeriod() && super.working();
    }

    // 处理普通消息，注意方法的返回值预示着是否继续运行节点
    protected abstract boolean resolveMassage(JSONObject json) throws NodeRunningException;
    // 处理Transaction
    protected abstract boolean resolveWait() throws NodeRunningException;
    // 处理无工作的空窗期
    protected abstract boolean resolveGapPeriod() throws NodeRunningException;
    
}
