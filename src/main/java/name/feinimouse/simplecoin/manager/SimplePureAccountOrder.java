package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.UserManager;

import java.util.List;

public class SimplePureAccountOrder extends SimpleOrder<Transaction> {

    public SimplePureAccountOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
        super(manager, transactions);
    }

    @Override
    public long activate() {
        processing = true;
        try {
            verifyTimes.clear();
            while (!allTrans.isEmpty()) {
                var transaction = allTrans.poll();
                if (super.verify(transaction)) {
                    orderQueue.add(transaction);
                } else {
                    throw new RuntimeException("交易验证失败");
                }
            }
            return verifyTimes.stream().reduce(Long::sum).orElse(0L);
        } finally {
            processing = false;
        }
    }
    
}
