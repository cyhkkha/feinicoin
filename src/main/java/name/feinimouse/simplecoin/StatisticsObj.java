package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.SimpleCenter;

import java.util.LinkedList;
import java.util.List;

public class StatisticsObj {
    private List<Float> runTimes;
    private List<Float> verifyTimes;
    private List<Integer> blockCount;
    private List<Integer> transSizes;
    
    public StatisticsObj() {
        runTimes = new LinkedList<>();
        verifyTimes = new LinkedList<>();
        blockCount = new LinkedList<>();
        transSizes = new LinkedList<>();
    }
    public void set(SimpleCenter center, int size) {
        runTimes.add(center.getRunTime() / 1000_000f);
        verifyTimes.add(center.getVerifyTime() / 1000_000f);
        blockCount.add(center.getBlockCounts());
        transSizes.add(size);
    }
    public void print(String name) {
        System.out.println("--------------------");
        System.out.println(name);
        System.out.println("====== size ======");
        transSizes.forEach(System.out::println);
        System.out.println("====== run time ======");
        runTimes.forEach(System.out::println);
        System.out.println("====== verify time ======");
        verifyTimes.forEach(System.out::println);
        System.out.println("====== block count ======");
        blockCount.forEach(System.out::println);
        System.out.println("--------------------");
    }
    
}
