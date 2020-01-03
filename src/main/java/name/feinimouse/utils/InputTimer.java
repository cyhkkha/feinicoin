package name.feinimouse.utils;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.lambda.InputRunner;

public class InputTimer<I> {
    private InputRunner<I> inputRunner;
    @Getter
    private long runTime;

    public InputTimer(InputRunner<I> runner) {
        inputRunner = runner;
    }

    public void start(I in) {
        if (inputRunner != null) {
            long startTime = System.currentTimeMillis();
            inputRunner.run(in);
            long endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
        }
    }
}
