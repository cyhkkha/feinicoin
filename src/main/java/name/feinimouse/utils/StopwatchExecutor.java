package name.feinimouse.utils;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.lambda.InputRunner;
import name.feinimouse.feinicoinplus.core.lambda.RunnerStopper;

import java.util.LinkedList;

public class StopwatchExecutor {
    @Getter
    // 限制时间
    private long maxRunTime;
    @Getter
    @Setter
    // 任务间隔
    private long intervalTime;

    @Getter
    // 每次任务完成时间
    private LinkedList<Long> taskRunTimeList;
    
    @Getter
    // 上一次完整流程的运行时间
    private long realRunTime;

    private boolean running = false;
    private InputRunner<RunnerStopper> runner;

    public StopwatchExecutor() {
        taskRunTimeList = new LinkedList<>();
    }

    public StopwatchExecutor(long maxRunTime, long intervalTime, InputRunner<RunnerStopper> runner) {
        this();
        this.maxRunTime = maxRunTime;
        this.intervalTime = intervalTime;
        this.runner = runner;
    }

    // 只有在停止的状态下才能重新设置任务
    public boolean setRunner(InputRunner<RunnerStopper> runner) {
        if (isStop()) {
            this.runner = runner;
            return true;
        }
        return false;
    }

    // 只有在停止的状态下才能重新设置运行最终时间
    public boolean setMaxRunTime(long maxETime) {
        if (isStop()) {
            this.maxRunTime = maxETime;
            return true;
        }
        return false;
    }

    public void start() {
        // 计算出停止时间
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + maxRunTime;
        long taskSumRunTime = 0;
        taskRunTimeList.clear();

        running = true;
        while (running) {
            long taskStartTime = System.currentTimeMillis();
            // 若开始时已经过了停止时间则直接停止
            if (isStop() || taskStartTime > stopTime) {
                break;
            }
            try {
                // 比较上一次的预测时间，若预测时间超出停止时间则不进行下一次任务，到时后自动停止
                if (taskSumRunTime != 0 && taskStartTime + taskSumRunTime/taskRunTimeList.size() > stopTime) {
                    Thread.sleep(stopTime - taskStartTime);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            runner.run(this::stopExecutor);
            
            long taskStopTime = System.currentTimeMillis();
            // 上次任务的执行时间存储起来
            long lastTaskRunTime = taskStopTime - taskStartTime;
            taskRunTimeList.add(lastTaskRunTime);
            taskSumRunTime += lastTaskRunTime;
            // 若任务结束时间过了停止时间则直接停止
            if (isStop() || taskStopTime > stopTime) {
                break;
            }
            try {
                // 间隔时间若大于停止时间则到停止时间自动停止
                if (taskStopTime + intervalTime > stopTime) {
                    Thread.sleep(stopTime - taskStopTime);
                    break;
                } else {
                    Thread.sleep(intervalTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        realRunTime = startTime - System.currentTimeMillis();
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
