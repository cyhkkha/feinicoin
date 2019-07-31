package name.feinimouse.simplecoin.manager;

import lombok.Getter;
import lombok.NonNull;
import name.feinimouse.simplecoin.core.UserManager;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleOrder <IN, OUT> extends SimpleVerifier {
    protected ConcurrentLinkedQueue<IN> allTrans;
    protected ConcurrentLinkedQueue<OUT> orderQueue;
    protected boolean processing = false;

    private AtomicBoolean isOutBlock = new AtomicBoolean(true);
    @Getter
    private boolean waiting = false;

    public void isOutBlock(boolean bool) {
        isOutBlock.set(bool);
    }
    
    public SimpleOrder(@NonNull UserManager manager, @NonNull List<IN> transactions) {
        super(manager);
        orderQueue = new ConcurrentLinkedQueue<>();
        allTrans = new ConcurrentLinkedQueue<>();
        allTrans.addAll(transactions);
    }
    
    public abstract long activate();

    public boolean isFinish() {
        return !processing && orderQueue.isEmpty();
    }
    
    
    public OUT pull() {
        return orderQueue.poll();
    }
    
    protected void waitOutBlock() {
        while (isOutBlock.get()) {
            if (!waiting) {
                waiting = true;
                System.out.println("waiting block out...");
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("验签线程意外中断");
            }
        }
        if (waiting) {
            waiting = false;
            System.out.println("verifying trans start...");
        }
    } 

}
