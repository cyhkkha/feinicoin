package name.feinimouse.utils;

import org.junit.Test;

import java.util.Arrays;

public class TestStopwatchUtils {
    @Test
    public void testStopwatch() {
        StopwatchUtils.Statistics result1 = StopwatchUtils.run(1000, this::myRun);
        System.out.println(result1.getTotalRunTime());
        System.out.println(Arrays.toString(result1.getRunTimes()));
        System.out.println(Arrays.stream(result1.getRunTimes()).reduce(0, Long::sum));
        System.out.println("------");

        StopwatchUtils.Statistics result2 = StopwatchUtils.run(1000, 10, this::myRun);
        System.out.println(result2.getTotalRunTime());
        System.out.println(Arrays.toString(result2.getRunTimes()));
        System.out.println(Arrays.stream(result2.getRunTimes()).reduce(0, Long::sum));
        System.out.println("------");

        StopwatchUtils.Result<String> result3 = StopwatchUtils.run(1000, this::myRun2);
        System.out.println(result3.getStatistics().getTotalRunTime());
        System.out.println(Arrays.toString(result3.getStatistics().getRunTimes()));
        System.out.println(Arrays.stream(result3.getStatistics().getRunTimes()).reduce(0, Long::sum));
        System.out.println(result3.get());
        System.out.println("------");

    }
    
    private void myRun() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private String myRun2() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(System.currentTimeMillis());
    }
}
