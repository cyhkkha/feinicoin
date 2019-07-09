package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.utils.LoopUtils;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleOrder extends SimpleVerifier {
    private ConcurrentLinkedQueue<Transaction> allTrans;
    private ConcurrentLinkedQueue<Transaction> transOrderQueue;
    private ConcurrentLinkedQueue<TransBundle> bundleOrderQueue;
    @Getter
    private boolean processing = false;
    @Getter @Setter
    private int bundleLimit = 10;
    
    public SimpleOrder(@NonNull UserManager manager, List<Transaction> transactions) {
        super(manager);
        transOrderQueue = new ConcurrentLinkedQueue<>();
        bundleOrderQueue = new ConcurrentLinkedQueue<>();
        allTrans = new ConcurrentLinkedQueue<>();
        allTrans.addAll(transactions);
    }
    
    private void activeUTXO() {
        processing = true;
        while (!allTrans.isEmpty()) {
            var transaction = allTrans.poll();
            if (super.verify(transaction)) {
                transOrderQueue.add(transaction);
            } else {
                throw new RuntimeException("交易验证失败");
            }
        }
        processing = false;
    }
    
    private void activeBCBDC() {
        processing = true;
        while (!allTrans.isEmpty()) {
            var bundleSource = LoopUtils.loopToListBreak(bundleLimit, allTrans::poll);
            bundleSource.forEach(item -> {
                if (! super.verify(item)) {
                    throw new RuntimeException("交易验证失败");
                }
            });
            var bundle = new TransBundle(bundleSource);
            var signedBundle = super.signBundle(bundle);
            bundleOrderQueue.add(signedBundle);
        }
        processing = false;
    }
    
    public boolean isFinish() {
        return !processing && transOrderQueue.isEmpty() && bundleOrderQueue.isEmpty();
    }
    
    public Transaction takeTransaction() {
        return transOrderQueue.poll();
    }
    public TransBundle takeBundle() {
        return bundleOrderQueue.poll();
    }
}
