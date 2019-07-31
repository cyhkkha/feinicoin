package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.account.MixedBundle;
import name.feinimouse.simplecoin.manager.SimpleCenter;

public class SimpleMixedBCBDCCenter extends SimpleCenter<MixedBundle> {
    
    public SimpleMixedBCBDCCenter(@NonNull SimpleMixedBCBDCOrder order) {
        super(order);
        setName("混合型BCBDC");
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
