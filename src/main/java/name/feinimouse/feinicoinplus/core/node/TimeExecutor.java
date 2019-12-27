package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.lambda.InputRunner;
import name.feinimouse.feinicoinplus.core.lambda.RunnerStopper;

public class TimeExecutor {
    @Getter
    // 限制时间
    private long maxETime;
    @Getter
    // 任务间隔
    private long delayETime;

    @Getter
    // 上一次单次任务的时间
    private long lastTaskETime;
    @Getter
    // 上一次完整流程的运行时间
    private long realETime;
    
    private boolean running = false;
    private InputRunner<RunnerStopper> runner;

    public boolean setRunner(InputRunner<RunnerStopper> runner) {
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
            
            runner.run(this::stopExecutor);
            
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
