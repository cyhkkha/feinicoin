package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.simplecoin.block.SimpleHashObj;

import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: SimpleCenter
 * Program : feinicoin
 * Description :
 */
public class SimpleUTXOCenter extends SimpleCenter {
    
    public SimpleUTXOCenter(@NonNull SimpleUTXOOrder order) {
        super(order);
    }
    
    @Override
    public void collectTransaction() {
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        // 清空当前块的交易列表
        bolckTransactionList.clear();
        do {// 取队列的交易
            var t = order.takeTransaction();
            if (t == null) {
                try {
//                    System.out.println("wait verify...");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException("线程意外终止");
                }
            } else  {
                // 推入交易到缓存
                var hashObj = new SimpleHashObj(
                    t.getSummary(), 
                    t.getHash(),
                    t.getSign()
                );
                bolckTransactionList.add(hashObj);

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
        
    }
    
    public List<SimpleHashObj> getCacheTransList() {
        return this.bolckTransactionList;
    }
    
}
