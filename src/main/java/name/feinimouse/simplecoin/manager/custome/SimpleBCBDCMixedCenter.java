package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.MixedBundle;
import name.feinimouse.simplecoin.manager.SimpleCenter;

public class SimpleBCBDCMixedCenter extends SimpleCenter<MixedBundle> {
    
    public SimpleBCBDCMixedCenter(@NonNull SimpleBCBDCMixedOrder order) {
        super(order);
    }

    @Override
    protected void collectTransaction() {
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        do {
            var mixed = order.pull();
            waitOrRun(mixed, () -> {
                if (mixed.isAssets()) {
                    var utxoBundle = mixed.getUtxo();
                    saveUTXOBundle(utxoBundle);
                } else {
                    var bundle = mixed.getTransBundle();
                    saveBundle(bundle);
                }
            });
            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
        System.out.println("collect time out...");
    }
    
}
