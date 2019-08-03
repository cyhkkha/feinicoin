package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.core.Config;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.manager.custome.SimplePureAccountCenter;
import name.feinimouse.simplecoin.manager.custome.SimplePureAccountOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;


public class RunPureAccount extends SimplecoinRunner {
    public RunPureAccount(Config config) {
        super(config);
    }
    
    @Override
    public StatisticsObj run() {
        preRun();

        var stat = new StatisticsObj();
        Arrays.asList(TEST_COUNT).forEach(size -> {
            var transList = LoopUtils.loopToList(size, transGen::genSignedTransFa);
            var order = new SimplePureAccountOrder(userManager, transList);
            var center = new SimplePureAccountCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        return stat;
    }
}
