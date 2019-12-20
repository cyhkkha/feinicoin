package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.manager.SimpleCenter;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: SimpleCenter
 * Program : feinicoin
 * Description :
 */
public class SimplePureAccountCenter extends SimpleCenter<Transaction> {
    
    public SimplePureAccountCenter(@NonNull SimplePureAccountOrder order) {
        super(order);
        setName("纯账户模式");
    }
    
    @Override
    protected void collectTransaction() {
        order.isOutBlock(false);
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        
        do {// 取队列的交易
            var t = order.pull();
            waitOrRun(t, () -> saveTransaction(t));
            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
//        System.out.println("collect time out...");
        order.isOutBlock(true);
    }
    
}
