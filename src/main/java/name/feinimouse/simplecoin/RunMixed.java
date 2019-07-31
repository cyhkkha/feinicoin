package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class RunMixed extends Config {
    private final static Integer[] testCount = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
    private final static int BUNDLE_SIZE = 20;
    private final static int UTXO_SIZE = 5;
    private final static double ASSET_RATE = 0.2;
    public static void main(String[] args) {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(testCount).forEach(size -> {
            var list = LoopUtils.loopToList((int)(size * (1 - ASSET_RATE)), () -> transGen.genMixedBundle());
            LoopUtils.loop((int)(size * ASSET_RATE), () -> list.add(transGen.genMixedBundle(UTXO_SIZE)));
            var order = new SimpleMixedBCBDCOrder(userManager, list, BUNDLE_SIZE);
            var center = new SimpleMixedBCBDCCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        stat.print("Mixed模式");
        System.exit(0);
    }
}
