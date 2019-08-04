package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.SimplecoinConfig;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class BCBDCUTXORunner extends SimplecoinRunner {
    
    public BCBDCUTXORunner(SimplecoinConfig simplecoinConfig) {
        super(simplecoinConfig);
    }

    @Override
    public StatisticsObj run() {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(TEST_COUNT).forEach(size -> {
            var list = LoopUtils.loopToList(size, () -> transGen.genMixedBundle(UTXO_SIZE));
            var order = new SimpleMixedBCBDCOrder(userManager, list);
            var center = new SimpleMixedBCBDCCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        return stat;
    }
    
}
