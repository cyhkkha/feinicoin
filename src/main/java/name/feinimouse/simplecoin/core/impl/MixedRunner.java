package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.SimplecoinConfig;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.Arrays;

public class MixedRunner extends SimplecoinRunner {

    public MixedRunner(SimplecoinConfig simplecoinConfig) {
        super(simplecoinConfig);
    }

    @Override
    public StatisticsObj run() {
        preRun();
        var stat = new StatisticsObj();
        Arrays.asList(TEST_COUNT).forEach(size -> {
            var transSize = (int) (size * (1 - ASSET_RATE));
            var assetSize = (int) (size * ASSET_RATE);
            var list = LoopUtils.loopToList(transSize, () -> transGen.genMixedBundle());
            LoopUtils.loop(assetSize, () -> list.add(transGen.genMixedBundle(UTXO_SIZE)));
            var order = new SimpleMixedBCBDCOrder(userManager, list, BUNDLE_SIZE);
            var center = new SimpleMixedBCBDCCenter(order);
            center.activate();
            stat.set(center, size);
            System.out.println(size + " is finished");
        });
        return stat;
    }
}
