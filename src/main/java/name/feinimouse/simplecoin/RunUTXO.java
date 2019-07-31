package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.custome.SimpleUTXOCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleUTXOOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class RunUTXO extends Config {
    private final static Integer[] testCount = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
    private final static int UTXO_SIZE = 5;
    public static void main(String[] args) {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(testCount).forEach(size -> {
            var list = LoopUtils.loopToList(size, () -> transGen.genUTXOBundle(UTXO_SIZE));
            var order = new SimpleUTXOOrder(userManager, list);
            var center = new SimpleUTXOCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        stat.print("纯UTXO模式");
        System.exit(0);
    }
}
