package name.feinimouse.utils;

import name.feinimouse.lambda.CustomRunner;
import name.feinimouse.lambda.InputRunner;
import name.feinimouse.lambda.OrdinaryRunner;
import name.feinimouse.lambda.ReturnRunner;

import java.util.Optional;

public class TimerUtils {
    public static class InternalTimerResult<T> implements TimerResult<T> {
        private T obj;
        private long time;

        public InternalTimerResult(long time, T obj) {
            this.time = time;
            this.obj = obj;
        }

        @Override
        public T get() {
            return obj;
        }

        @Override
        public long getTime() {
            return time;
        }
    }

    public static TimerResult<?> run(OrdinaryRunner runner) {
        return Optional.ofNullable(runner)
            .map(r -> {
                long startTime = System.currentTimeMillis();
                r.run();
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                return new InternalTimerResult<>(runTime, null);
            }).orElse(null);
    }

    public static <T> TimerResult<T> run(ReturnRunner<T> runner) {
        return Optional.ofNullable(runner)
            .map(r -> {
                long startTime = System.currentTimeMillis();
                T result = r.run();
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                return new InternalTimerResult<>(runTime, result);
            }).orElse(null);
    }

    public static <T> TimerResult<?> run(InputRunner<T> runner, T input) {
        return getRunner(runner).run(input);
    }

    public static <I, O> TimerResult<O> run(CustomRunner<I, O> runner, I input) {
        return getRunner(runner).run(input);
    }
    
    public static <I> CustomRunner<I, TimerResult<?>> getRunner(InputRunner<I> runner) {
        return Optional.ofNullable(runner)
            .map(r -> (CustomRunner<I, TimerResult<?>>) input -> {
                long startTime = System.currentTimeMillis();
                r.run(input);
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                return new InternalTimerResult<>(runTime, null);
            }).orElse(null);
    }

    public static <I, O> CustomRunner<I, TimerResult<O>> getRunner(CustomRunner<I, O> runner) {
        return Optional.ofNullable(runner)
            .map(r -> (CustomRunner<I, TimerResult<O>>) input -> {
                long startTime = System.currentTimeMillis();
                O result = r.run(input);
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                return new InternalTimerResult<>(runTime, result);
            }).orElse(null);
    }

}
