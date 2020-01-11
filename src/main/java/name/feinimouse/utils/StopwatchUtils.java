package name.feinimouse.utils;

import name.feinimouse.lambda.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 该工具能够在一个 *时间段* 内不断地 *重复* 执行某个任务
 * @author  Cao Feinimouse
 */
public class StopwatchUtils {
    public static final long DEFAULT_INTERVAL_TIME = 0;

    /**
     * 在一个时间段内重复执行一个无返回值的任务
     * @param maxRunTime 时间段大小
     * @param runner 任务的具体内容，这是一个lambda表达式
     * @return 任务报告，包括任务的具体执行时间和每次子任务的执行时间
     */
    public static Statistics run(long maxRunTime, OrdinaryRunner runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, runner);
    }

    /**
     * 同上，可以设置任务执行间隔
     * @param maxRunTime 同上
     * @param intervalTime 每个任务执行的间隔时间
     * @param runner 同上
     * @return 同上
     */
    public static Statistics run(long maxRunTime, long intervalTime, OrdinaryRunner runner) {
        return run(maxRunTime, intervalTime, stopper -> {
            runner.run();
        });
    }

    /**
     * 在一个时间段内重复执行一个 *有* 返回值的任务
     * @param maxRunTime 同上
     * @param runner 同上
     * @param <T> 返回值的类型
     * @return 包含每次任务的执行结果，以及任务报告
     */
    public static <T> Result<T> run(long maxRunTime, ReturnRunner<T> runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, runner);
    }
    
    /** 同上 */
    public static <T> Result<T> run(long maxRunTime, long intervalTime, ReturnRunner<T> runner) {
        return run(maxRunTime, intervalTime, stopper -> {
            return runner.run();
        });
    }

    /**
     * 在一个时间段内重复执行一个无返回值的任务，在任务途中可以中断
     * RunnerStopper 是一个执行器，包含 stop() 方法，能够中断秒表的运行
     * @param maxRunTime 同上
     * @param runner 同上
     * @return 同上
     */
    public static Statistics run(long maxRunTime, InputRunner<RunnerStopper> runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, runner);
    }

    /**
     * 在一个时间段内重复执行一个 *有* 返回值的任务，在任务途中可以中断
     * RunnerStopper 是一个执行器，包含 stop() 方法，能够中断秒表的运行
     * @param maxRunTime 同上
     * @param runner 同上
     * @param <T> 返回值的类型
     * @return 包含每次任务的执行结果，以及任务报告
     */
    public static <T> Result<T> run(long maxRunTime, CustomRunner<RunnerStopper, T> runner) {
        return run(maxRunTime, DEFAULT_INTERVAL_TIME, runner);
    }

    /** 同上 */
    public static <T> Result<T> run(long maxRunTime, long intervalTime
        , CustomRunner<RunnerStopper, T> runner) {
        if (runner == null) {
            return null;
        }
        List<T> list = new LinkedList<>();
        Statistics statistics = run(maxRunTime, intervalTime, stopper -> {
            T r = runner.run(stopper);
            list.add(r);
        });
        return new Result<>(statistics, list);
    }

    /** 同上 */
    public static Statistics run(long maxRunTime, long intervalTime
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
                if (taskSumRunTime != 0 && (taskStartTime + (taskSumRunTime / taskRunTimeList.size())) > stopTime) {
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
                } else if (intervalTime > 0) {
                    Thread.sleep(intervalTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        long realRunTime = System.currentTimeMillis() - startTime;
        return new Statistics(realRunTime
            , taskRunTimeList.stream().mapToLong(i -> i).toArray());
    }

    public static class Statistics {
        private long totalRunTime;
        private long[] runTimes;

        public Statistics(long totalRunTime, long[] runTimes) {
            this.totalRunTime = totalRunTime;
            this.runTimes = runTimes;
        }

        public long getTotalRunTime() {
            return totalRunTime;
        }
        
        public long[] getRunTimes() {
            return runTimes;
        }
    }
    
    public static class Result<T> {
        private Statistics statistics;
        private List<T> result;
        
        public Result(Statistics statistics, List<T> result) {
            this.statistics = statistics;
            this.result = result;
        }

        public Statistics getStatistics() {
            return statistics;
        }

        public List<T> get() {
            return result;
        }
    }
    
}
