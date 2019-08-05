package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.SimplecoinConfig;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCAccountCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCAccountOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class BCBDCAccountRunner extends SimplecoinRunner {
    
    public BCBDCAccountRunner(SimplecoinConfig simplecoinConfig) {
        super(simplecoinConfig);
    }

    @Override
    public StatisticsObj run() {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(TEST_COUNT).forEach(size -> {
            var list = LoopUtils.loopToList(size, transGen::genSignedTransFa);
            var order = new SimpleBCBDCAccountOrder(userManager, list, BUNDLE_SIZE);
            var center = new SimpleBCBDCAccountCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        return stat;
    }
}
