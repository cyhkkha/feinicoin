package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.account.UTXOBundle;
import name.feinimouse.simplecoin.manager.SimpleCenter;

public class SimpleUTXOCenter extends SimpleCenter<UTXOBundle> {
    
    public SimpleUTXOCenter(@NonNull SimpleUTXOOrder order) {
        super(order);
    }

    @Override
    protected void collectTransaction() {
        order.isOutBlock(false);
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        do {// 取Order队列
            var utxoBundle = order.pull();
            waitOrRun(utxoBundle, () -> saveUTXOBundle(utxoBundle));
            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
        System.out.println("collect time out...");
        order.isOutBlock(true);
    }
}
