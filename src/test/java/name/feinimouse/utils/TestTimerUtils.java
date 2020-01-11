package name.feinimouse.utils;

import org.junit.Assert;
import org.junit.Test;

public class TestTimerUtils {
    @Test
    public void testTimerUtils() {
        long result1 = TimerUtils.run(this::myRun);
        System.out.println(result1);

        TimerUtils.Result<Integer> result2 = TimerUtils.run(() -> {
            myRun();
            return 100;
        });
        System.out.println(result2.getTime());
        System.out.println(result2.get());
        Assert.assertEquals(result2.get().intValue(), 100);

        TimerUtils.Result<String> result3 = TimerUtils.run(test -> {
            myRun();
            return test;
        }, "test");
        System.out.println(result3.getTime());
        System.out.println(result3.get());
        Assert.assertEquals(result3.get(), "test");

        var runner = TimerUtils.getRunner(test -> {
            myRun();
            return "test" + test;
        });
        TimerUtils.Result<String> result4 = runner.run("12345");
        System.out.println(result4.getTime());
        System.out.println(result4.get());
    }
    
    private void myRun() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
