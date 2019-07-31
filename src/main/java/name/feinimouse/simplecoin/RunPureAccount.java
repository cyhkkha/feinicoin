package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.custome.SimplePureAccountCenter;
import name.feinimouse.simplecoin.manager.custome.SimplePureAccountOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;


public class RunPureAccount extends Config {
    private final static Integer[] testCount = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
    public static void main(String[] args) {
        preRun();
        
        var stat = new StatisticsObj();
        Arrays.asList(testCount).forEach(size -> {
            var transList = LoopUtils.loopToList(size, transGen::genSignedTransFa);
            var order = new SimplePureAccountOrder(userManager, transList);
            var center = new SimplePureAccountCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        stat.print("纯账户模式");
        System.exit(0);
    }
}
