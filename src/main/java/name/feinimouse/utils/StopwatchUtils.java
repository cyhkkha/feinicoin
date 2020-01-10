package name.feinimouse.utils;

import name.feinimouse.lambda.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StopwatchUtils {
    public static final long DEFAULT_INTERVAL_TIME = 10;

    public static StopwatchResult<?> run(long maxRunTime, OrdinaryRunner runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, stopper -> { runner.run(); });
    }
    
    public static StopwatchResult<?> run(long maxRunTime, long intervalTime, OrdinaryRunner runner) {
        return run(maxRunTime, intervalTime, stopper -> { runner.run(); });
    }

    public static StopwatchResult<?> run(long maxRunTime, InputRunner<RunnerStopper> runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, runner);
    }

    public static <T> StopwatchResult<List<T>> run(long maxRunTime, CustomRunner<RunnerStopper, T> runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, runner);
    }

    public static <T> StopwatchResult<List<T>> run(long maxRunTime, long intervalTime
        , CustomRunner<RunnerStopper, T> runner) {
        if (runner == null) {
            return null;
        }
        List<T> list = new LinkedList<>();
        StopwatchResult<?> result = run(maxRunTime, intervalTime, stopper -> {
            T r = runner.run(stopper);
            list.add(r);
        });
        return new InternalResult<>(list, result);
    }

    public static StopwatchResult<?> run(long maxRunTime, long intervalTime
        , InputRunner<RunnerStopper> runner) {
        if (runner == null) {
            return null;
        }
        // 计算出停止时间
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + maxRunTime;
        long taskSumRunTime = 0;
        List<Long> taskRunTimeList = new LinkedList<>();

        AtomicBoolean running = new AtomicBoolean(true);
        while (running.get()) {
            long taskStartTime = System.currentTimeMillis();
            // 若开始时已经过了停止时间则直接停止
            if (!running.get() || taskStartTime > stopTime) {
                break;
            }
            try {
                // 比较上一次的预测时间，若预测时间超出停止时间则不进行下一次任务，到时后自动停止
                if (taskSumRunTime != 0 && taskStartTime + taskSumRunTime / taskRunTimeList.size() > stopTime) {
                    Thread.sleep(stopTime - taskStartTime);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            runner.run(() -> {
                if (running.get()) {
                    running.set(false);
                }
            });

            long taskStopTime = System.currentTimeMillis();
            // 上次任务的执行时间存储起来
            long lastTaskRunTime = taskStopTime - taskStartTime;
            taskRunTimeList.add(lastTaskRunTime);
            taskSumRunTime += lastTaskRunTime;
            // 若任务结束时间过了停止时间则直接停止
            if (!running.get() || taskStopTime > stopTime) {
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
        long realRunTime = startTime - System.currentTimeMillis();
        return new InternalResult<>(null, realRunTime
            , taskRunTimeList.stream().mapToLong(i -> i).toArray());
    }

    public static class InternalResult<T> implements StopwatchResult<T> {
        private T obj;
        private long totalRunTime;
        private long expectRunTime;
        private long[] runTimes;

        public InternalResult(T obj, long totalRunTime, long[] runTimes) {
            this.obj = obj;
            this.totalRunTime = totalRunTime;
            this.runTimes = runTimes;
        }

        public InternalResult(T obj, StopwatchResult<?> stopwatchResult) {
            this.obj = obj;
            totalRunTime = stopwatchResult.getTotalRunTime();
            runTimes = stopwatchResult.getRunTimes();
        }

        @Override
        public T get() {
            return obj;
        }

        @Override
        public long getTotalRunTime() {
            return totalRunTime;
        }


        @Override
        public long[] getRunTimes() {
            return runTimes;
        }
    }
}
