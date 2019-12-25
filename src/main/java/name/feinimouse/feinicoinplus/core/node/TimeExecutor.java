package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.feinicoinplus.core.lambda.OrdinaryRunner;

public class TimeExecutor {
    @Getter
    // 限制时间
    private long maxETime;
    @Getter
    // 上一次单次任务的时间
    private long lastTaskETime;
    @Getter
    // 上一次完整流程的运行时间
    private long realETime;
    @Getter
    // 任务间隔
    private long delayETime;
    
    private boolean running = false;
    private OrdinaryRunner runner;
    
    public TimeExecutor(long maxETime) {
        this.maxETime = maxETime;
    }    
    public boolean setRunner(OrdinaryRunner runner) {
        if (isStop()) {
            this.runner = runner;
            return true;
        }
        return false;
    }
    public boolean setMaxETime(long maxETime) {
        if (isStop()) {
            this.maxETime = maxETime;
            return true;
        }
        return false;
    }

    public boolean setDelayETime(long delayETime) {
        if (isStop()) {
            this.delayETime = delayETime;
            return true;
        }
        return false;
    }

    public void start() {
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + maxETime;
        running = true;
        while (running) {
            long taskStartTime = System.currentTimeMillis();
            try {
                if (taskStartTime + lastTaskETime > stopTime) {
                    Thread.sleep(stopTime - taskStartTime);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            
            runner.run();
            
            long taskStopTime = System.currentTimeMillis();
            lastTaskETime = taskStopTime - taskStartTime;
            if (isStop()) {
                break;
            }
            try {
                if (taskStopTime + delayETime > stopTime) {
                    Thread.sleep(stopTime - taskStopTime);
                    break;
                } else {
                    Thread.sleep(delayETime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        realETime = startTime - System.currentTimeMillis();
        running = false;
    }
    void stopExecutor() {
        if (running) {
            running = false;    
        }
    }
    boolean isStop() {
        return !running;
    }
}
