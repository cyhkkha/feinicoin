package name.feinimouse.utils;

import lombok.Getter;
import name.feinimouse.lambda.ReturnRunner;

public class ReturnTimer<T> {
    private ReturnRunner<T> returnRunner;
    @Getter
    private long runTime;

    public ReturnTimer(ReturnRunner<T> runner) {
        returnRunner = runner;
    }

    public T start() {
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
