package name.feinimouse.simplecoin.manager.custome;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.account.TransBundle;
import name.feinimouse.simplecoin.core.UserManager;
import name.feinimouse.simplecoin.manager.SimpleOrder;
import name.feinimouse.utils.LoopUtils;

import java.util.List;

public class SimpleBCBDCAccountOrder extends SimpleOrder<Transaction, TransBundle> {
    @Getter @Setter
    protected int bundleLimit = 10;

    public SimpleBCBDCAccountOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions, int bundleLimit) {
        this(manager, transactions);
        setBundleLimit(bundleLimit);
    }
    
    public SimpleBCBDCAccountOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
        super(manager, transactions);
    }

    @Override
    public long activate() {
        processing = true;
        try {
            verifyTimes.clear();
            bundleTimes.clear();
            while (!allTrans.isEmpty()) {
                var bundleSource = LoopUtils.loopToListBreak(bundleLimit, allTrans::poll);
                bundleSource.forEach(item -> {
                    if (!super.verify(item)) {
                        throw new RuntimeException("交易验证失败");
                    }
                });
                var bundle = new TransBundle(bundleSource);
                var signedBundle = super.signBundle(bundle);
                orderQueue.add(signedBundle);
            }
            return verifyTimes.stream().reduce(Long::sum).orElse(0L)
                + bundleTimes.stream().reduce(Long::sum).orElse(0L);
        } finally {
            processing = false;
        }
    }
}
