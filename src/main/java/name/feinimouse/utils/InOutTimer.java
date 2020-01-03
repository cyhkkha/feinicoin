package name.feinimouse.utils;

import lombok.Getter;
import name.feinimouse.lambda.InOutRunner;

public class InOutTimer<I, O> {
    private InOutRunner<I, O> ioInOutRunner;
    @Getter
    private long runTime;

    public InOutTimer(InOutRunner<I, O> runner) {
        ioInOutRunner = runner;
    }

    public O start(I in) {
        if (ioInOutRunner != null) {
            long startTime = System.currentTimeMillis();
            O result = ioInOutRunner.run(in);
            long endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
            return result;
        }
        return null;
    }
}
