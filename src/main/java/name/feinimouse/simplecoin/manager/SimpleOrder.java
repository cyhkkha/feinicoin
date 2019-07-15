package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.UserManager;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class SimpleOrder <T> extends SimpleVerifier {
    protected ConcurrentLinkedQueue<Transaction> allTrans;
    protected ConcurrentLinkedQueue<T> orderQueue;
    protected boolean processing = false;
    
    public SimpleOrder(@NonNull UserManager manager, @NonNull List<Transaction> transactions) {
        super(manager);
        orderQueue = new ConcurrentLinkedQueue<>();
        allTrans = new ConcurrentLinkedQueue<>();
        allTrans.addAll(transactions);
    }
    
    public abstract long activate();

    public boolean isFinish() {
        return !processing && orderQueue.isEmpty();
    }
    
    
    public T pull() {
        return orderQueue.poll();
    }

}
