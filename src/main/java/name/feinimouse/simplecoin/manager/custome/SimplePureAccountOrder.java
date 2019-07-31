package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.UserManager;
import name.feinimouse.simplecoin.manager.SimpleOrder;

import java.util.List;

public class SimplePureAccountOrder extends SimpleOrder<Transaction, Transaction> {

    public SimplePureAccountOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
        super(manager, transactions);
    }
    
    @Override
    public long activate() {
        processing = true;
            verifyTimes.clear();
            Transaction transaction;
            while ((transaction = allTrans.poll()) != null) {
                waitOutBlock();
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
