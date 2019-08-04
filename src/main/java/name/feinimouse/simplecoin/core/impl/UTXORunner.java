package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.SimplecoinConfig;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.manager.custome.SimpleUTXOCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleUTXOOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class UTXORunner extends SimplecoinRunner {
    public UTXORunner(SimplecoinConfig simplecoinConfig) {
        super(simplecoinConfig);
    }

    @Override
    public StatisticsObj run() {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(TEST_COUNT).forEach(size -> {
            var list = LoopUtils.loopToList(size, () -> transGen.genUTXOBundle(UTXO_SIZE));
            var order = new SimpleUTXOOrder(userManager, list);
            var center = new SimpleUTXOCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        return stat;
    }
    
}
