package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.TransBundle;
import name.feinimouse.simplecoin.UserManager;
import name.feinimouse.utils.LoopUtils;

import java.util.List;

public class SimpleBCBDCOrder extends SimpleOrder {

    public SimpleBCBDCOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
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
                bundleOrderQueue.add(signedBundle);
            }
            return verifyTimes.stream().reduce(Long::sum).orElse(0L)
                + bundleTimes.stream().reduce(Long::sum).orElse(0L);
        } finally {
            processing = false;
        }
    }
}
