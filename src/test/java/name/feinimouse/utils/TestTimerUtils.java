package name.feinimouse.utils;

import name.feinimouse.lambda.CustomRunner;
import org.junit.Assert;
import org.junit.Test;

public class TestTimerUtils {
    @Test
    public void testTimerUtils() {
        TimerResult<?> result1 = TimerUtils.run(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(result1.getTime());
        System.out.println(result1.get());
        
        TimerResult<Integer> result2 = TimerUtils.run(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 100;
        });
        System.out.println(result2.getTime());
        System.out.println(result2.get());
        Assert.assertEquals(result2.get().intValue(), 100);

        TimerResult<String> result3 = TimerUtils.run(test -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return test;
        }, "test");
        System.out.println(result3.getTime());
        System.out.println(result3.get());
        Assert.assertEquals(result3.get(), "test");

        var runner = TimerUtils.getRunner(test -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "test" + test;
        });
        TimerResult<String> result4 = runner.run("12345");
        System.out.println(result4.getTime());
        System.out.println(result4.get());
    }
}
