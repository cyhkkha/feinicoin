package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.block.MongoDao;
import name.feinimouse.simplecoin.block.SimpleHashObj;

import java.util.List;

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
    }
    
    @Override
    public void collectTransaction() {
        var order = (SimplePureAccountOrder)super.order;
        order.isOutBlock(false);
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        
        do {// 取队列的交易
            var t = order.pull();
            if (t == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException("线程意外终止");
                }
            } else  {
                // 存入交易
                MongoDao.insertTrans(super.blockNumber, new SimpleHashObj(t).toDocument());

                // 更新账户缓存
                var sender = t.getSender();
                var receiver = t.getReceiver();
                var coin = (Integer)t.getCoin();
                blockAccountMap.merge(sender, - coin, Integer::sum);
                blockAccountMap.merge(receiver, coin, Integer::sum);
            }
            
            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
        System.out.println("collect time out...");
        order.isOutBlock(true);
    }
    
}
