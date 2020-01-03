package name.feinimouse.utils;

import lombok.Getter;
import name.feinimouse.lambda.OrdinaryRunner;

public class OrdinaryTimer {
    private OrdinaryRunner ordinaryRunner;
    @Getter
    private long runTime;
    public OrdinaryTimer(OrdinaryRunner runner) {
        ordinaryRunner = runner;
    }
    
    public void start() {
        if (ordinaryRunner != null) {
            long startTime = System.currentTimeMillis();
            ordinaryRunner.run();
            long endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
        }
    }
}
