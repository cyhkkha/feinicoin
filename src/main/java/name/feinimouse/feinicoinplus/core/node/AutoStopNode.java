package name.feinimouse.feinicoinplus.core.node;


import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.node.exception.NodeRunningException;
import name.feinimouse.feinicoinplus.core.node.exception.NodeStopException;

public abstract class AutoStopNode extends BaseNode {

    @Getter
    private long startTime;
    @Getter
    private long stopTime;

    // 空窗期开始时间
    private long gapStartTime;
    // 最大工作时间(默认20秒)
    @Setter
    protected long maxGapTime = 5 * 1000;

    public AutoStopNode(String nodeType) {
        super(nodeType);
    }

    public long getWorkingTime() {
        return stopTime == 0
            ? System.currentTimeMillis() - startTime
            : stopTime - startTime;
    }

    protected final void resetGap() {
        gapStartTime = System.currentTimeMillis();
    }

    // 超过工作时间则退出
    @Override
    protected void working() throws NodeRunningException {
        // 先执行空窗任务，再判断是否空窗超时
        gapWorking();
        if (System.currentTimeMillis() - gapStartTime >= maxGapTime) {
            throw new NodeStopException(this, "Node gap time out !!");
        }
    }

    @Override
    protected void beforeWork() {
        startTime = System.currentTimeMillis();
        startTime = 0;
        resetGap();
    }

    @Override
    protected void afterWork() {
        super.afterWork();
        stopTime = System.currentTimeMillis();
    }

    // 处理空窗期任务
    protected abstract void gapWorking() throws NodeRunningException;
}
