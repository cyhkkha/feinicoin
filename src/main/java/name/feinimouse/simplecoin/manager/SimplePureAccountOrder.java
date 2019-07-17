package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.UserManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimplePureAccountOrder extends SimpleOrder<Transaction> {

    public SimplePureAccountOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
        super(manager, transactions);
    }

    private AtomicBoolean isOutBlock = new AtomicBoolean(true);
    
    public void isOutBlock(boolean bool) {
        isOutBlock.set(bool);
    }
    
    @Override
    public long activate() {
        processing = true;
            verifyTimes.clear();
            Transaction transaction; 
            while ((transaction = allTrans.poll()) != null) {
                while (isOutBlock.get()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException("验签线程意外中断");
                    }
                } 
                if (super.verify(transaction)) {
                    orderQueue.add(transaction);
                } else {
                    throw new RuntimeException("交易验证失败");
                }
            }
            processing = false;
            return verifyTimes.stream().reduce(Long::sum).orElse(0L);
    }
    
}
