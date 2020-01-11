package name.feinimouse.utils;

import name.feinimouse.lambda.*;

import java.util.Optional;

public class TimerUtils {

    public static Long run(OrdinaryRunner runner) {
        return Optional.ofNullable(runner)
            .map(r -> {
                long startTime = System.currentTimeMillis();
                r.run();
                long endTime = System.currentTimeMillis();
                return endTime - startTime;
            }).orElse(null);
    }

    public static <T> Result<T> run(ReturnRunner<T> runner) {
        return Optional.ofNullable(runner)
            .map(r -> {
                long startTime = System.currentTimeMillis();
                T result = r.run();
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                return new Result<>(runTime, result);
            }).orElse(null);
    }

    public static <T> Long run(InputRunner<T> runner, T input) {
        return getRunner(runner).run(input);
    }

    public static <I, O> Result<O> run(CustomRunner<I, O> runner, I input) {
        return getRunner(runner).run(input);
    }

    public static <I> CustomRunner<I, Long> getRunner(InputRunner<I> runner) {
        return Optional.ofNullable(runner)
            .map(r -> (CustomRunner<I, Long>) input -> {
                long startTime = System.currentTimeMillis();
                r.run(input);
                long endTime = System.currentTimeMillis();
                return endTime - startTime;
            }).orElse(null);
    }

    public static <I, O> CustomRunner<I, Result<O>> getRunner(CustomRunner<I, O> runner) {
        return Optional.ofNullable(runner)
            .map(r -> (CustomRunner<I, Result<O>>) input -> {
                long startTime = System.currentTimeMillis();
                O result = r.run(input);
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                return new Result<>(runTime, result);
            }).orElse(null);
    }

    public static class Result<T> {
        private T obj;
        private long time;

        public Result(long time, T obj) {
            this.time = time;
            this.obj = obj;
        }

        public T get() {
            return obj;
        }

        public long getTime() {
            return time;
        }
    }
    
}
