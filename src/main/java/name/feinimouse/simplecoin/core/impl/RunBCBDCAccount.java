package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.core.Config;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class RunBCBDCAccount extends SimplecoinRunner {
    
    public RunBCBDCAccount(Config config) {
        super(config);
    }

    @Override
    public StatisticsObj run() {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(TEST_COUNT).forEach(size -> {
            var list = LoopUtils.loopToList(size, transGen::genSignedTransFa);
            var order = new SimpleBCBDCOrder(userManager, list, BUNDLE_SIZE);
            var center = new SimpleBCBDCCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        return stat;
    }
}
