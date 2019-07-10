package name.feinimouse.simplecoin.manager;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.TransBundle;
import name.feinimouse.simplecoin.UserManager;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class SimpleOrder extends SimpleVerifier {
    protected ConcurrentLinkedQueue<Transaction> allTrans;
    protected ConcurrentLinkedQueue<Transaction> transOrderQueue;
    protected ConcurrentLinkedQueue<TransBundle> bundleOrderQueue;
    protected boolean processing = false;
    @Getter @Setter
    protected int bundleLimit = 10;
    
    public SimpleOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
        super(manager);
        transOrderQueue = new ConcurrentLinkedQueue<>();
        bundleOrderQueue = new ConcurrentLinkedQueue<>();
        allTrans = new ConcurrentLinkedQueue<>();
        allTrans.addAll(transactions);
    }
    
    public abstract long activate();
    
    
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
