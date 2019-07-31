package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.UTXOBundle;
import name.feinimouse.simplecoin.block.SimpleHashObj;
import name.feinimouse.simplecoin.manager.SimpleCenter;
import name.feinimouse.simplecoin.mongodao.AssetsDao;
import name.feinimouse.simplecoin.mongodao.TransDao;

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
            if (utxoBundle == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException("线程意外终止");
                }
            } else  {
                // 存入交易
                while (!utxoBundle.isEmpty()) {
                    TransDao.insertList(super.blockNumber, new SimpleHashObj(utxoBundle.poll()).toDocument());
                }
                // 存入UTXO记录
                AssetsDao.insertList(super.blockNumber, new SimpleHashObj(utxoBundle).toDocument());
            }

            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
        System.out.println("collect time out...");
        order.isOutBlock(true);
    }
}
