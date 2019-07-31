package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class RunBCBDCAccount extends Config {
    private final static Integer[] testCount = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
    private final static int BUNDLE_SIZE = 20;
    public static void main(String[] args) {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(testCount).forEach(size -> {
            var list = LoopUtils.loopToList(size, transGen::genSignedTransFa);
            var order = new SimpleBCBDCOrder(userManager, list, BUNDLE_SIZE);
            var center = new SimpleBCBDCCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        stat.print("BCBDC账户模式");
        System.exit(0);
    }
}
