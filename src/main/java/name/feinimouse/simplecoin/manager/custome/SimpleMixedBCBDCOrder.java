package name.feinimouse.simplecoin.manager.custome;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.account.MixedBundle;
import name.feinimouse.simplecoin.account.TransBundle;
import name.feinimouse.simplecoin.core.UserManager;
import name.feinimouse.simplecoin.manager.SimpleOrder;

import java.util.LinkedList;
import java.util.List;

public class SimpleMixedBCBDCOrder extends SimpleOrder<MixedBundle, MixedBundle> {
    @Getter @Setter
    protected int bundleLimit = 10;

    public SimpleMixedBCBDCOrder(@NonNull UserManager manager, @NonNull List<MixedBundle> mixedList, int bundleLimit) {
        this(manager, mixedList);
        setBundleLimit(bundleLimit);
    }
    
    public SimpleMixedBCBDCOrder(@NonNull UserManager manager, @NonNull List<MixedBundle> mixedList) {
        super(manager, mixedList);
    }

    @Override
    public long activate() {
        processing = true;
        try {
            verifyTimes.clear();
            bundleTimes.clear();
            var bundleSource = new LinkedList<Transaction>();
            while (!allTrans.isEmpty()) {
                var mixedBundle = allTrans.poll();
                // 如果是资产则直接验证并添加
                if (mixedBundle.isAssets()) {
                    var utxoBundle = mixedBundle.getUtxo();
                    utxoBundle.forEach(trans -> {
                        if (!super.verify(trans, utxoBundle.getOwner())) {
                            throw new RuntimeException("交易验证失败");
                        }
                    });
                    orderQueue.add(mixedBundle);
                // 如果是交易则进行打包    
                } else {
                    var trans = mixedBundle.getTransaction();
                    if (!super.verify(trans)) {
                        throw new RuntimeException("交易验证失败");
                    }
                    bundleSource.add(trans);
                }
                // 如果打包数量够了则进行添加
                if (bundleSource.size() >= bundleLimit) {
                    var bundle = new TransBundle(bundleSource);
                    var signedBundle = super.signBundle(bundle);
                    orderQueue.add(new MixedBundle(signedBundle));
                    bundleSource = new LinkedList<>();
                }
            }
            // 如果还存在数据则直接打包并添加
            if (bundleSource.size() > 0) {
                var bundle = new TransBundle(bundleSource);
                var signedBundle = super.signBundle(bundle);
                orderQueue.add(new MixedBundle(signedBundle));
            }
            // 返回验证和打包时间
            return verifyTimes.stream().reduce(Long::sum).orElse(0L)
                + bundleTimes.stream().reduce(Long::sum).orElse(0L);
        } finally {
            processing = false;
        }
    }
}
