package name.feinimouse.utils;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.lambda.OrdinaryRunner;
import name.feinimouse.feinicoinplus.core.lambda.ReturnRunner;

public class TimerExecutor<T> {
    private OrdinaryRunner ordinaryRunner;
    private ReturnRunner<T> returnRunner;
    @Getter
    private long runTime;
    public TimerExecutor(OrdinaryRunner runner) {
        ordinaryRunner = runner;
    }
    public TimerExecutor(ReturnRunner<T> runner) {
        returnRunner = runner;
    }
    public T start() {
        if (ordinaryRunner != null) {
            long startTime = System.currentTimeMillis();
            ordinaryRunner.run();
            long endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
            return null;
        }
        if (returnRunner != null) {
            long startTime = System.currentTimeMillis();
            T result = returnRunner.run();
            long endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
            return result;
        }
        return null;
    }
}
