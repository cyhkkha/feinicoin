package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleOrder extends SimpleVerifier {
    private ConcurrentLinkedQueue<Transaction> transQueue;
    private ConcurrentLinkedQueue<TransBundle> bundleQueue;
    private boolean hasVrifing = false;
    
    public SimpleOrder(UserManager manager) {
        super(manager);
        transQueue = new ConcurrentLinkedQueue<>();
        bundleQueue = new ConcurrentLinkedQueue<>();
    }
    
    public void active() {
    }
    
    public void saveTransaction(@NonNull Transaction t) {
        transQueue.add(t);
    }
    public void saveBundle(@NonNull TransBundle b) {
        bundleQueue.add(b);
    }
    public Transaction takeTransaction() {
        return transQueue.poll();
    }
    public TransBundle takeBundle() {
        return bundleQueue.poll();
    }
}
