package name.feinimouse.feinicoinplus.core.node;


import lombok.Getter;
import lombok.Setter;

public abstract class AutoStopNode extends Node {

    @Getter
    private long startTime;
    @Getter
    private long stopTime;

    // 空窗期开始时间
    private long gapStartTime;
    // 最大工作时间(默认20秒)
    @Setter
    protected long maxGapTime = 20 * 1000;
    
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
    protected boolean working() {
        return System.currentTimeMillis() - gapStartTime <= maxGapTime;
    }

    @Override
    protected void beforeWork() {
        startTime = System.currentTimeMillis();
        resetGap();
    }

    @Override
    protected void afterWork() {
        stopTime = System.currentTimeMillis();
    }
}
