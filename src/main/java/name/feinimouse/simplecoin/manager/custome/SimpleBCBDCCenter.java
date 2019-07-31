package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.account.TransBundle;
import name.feinimouse.simplecoin.manager.SimpleCenter;

public class SimpleBCBDCCenter extends SimpleCenter<TransBundle> {
    
    public SimpleBCBDCCenter(@NonNull SimpleBCBDCOrder order) {
        super(order);
    }

    @Override
    protected void collectTransaction() {
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        do {
            var bundle = order.pull();
            waitOrRun(bundle, () -> saveBundle(bundle));
            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
        System.out.println("collect time out...");
    }
    
}
