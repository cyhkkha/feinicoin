package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.simplecoin.core.UserManager;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleOrder <IN, OUT> extends SimpleVerifier {
    protected ConcurrentLinkedQueue<IN> allTrans;
    protected ConcurrentLinkedQueue<OUT> orderQueue;
    protected boolean processing = false;

    // 是否正在出块
    private AtomicBoolean isOutBlock = new AtomicBoolean(true);
    // 是否在阻塞，用于监控和打印状态
    private AtomicBoolean waiting = new AtomicBoolean(false);

    /**
     * 构造一个Order
     * @param manager 用户管理器
     * @param transactions 初始化的交易
     */
    public SimpleOrder(@NonNull UserManager manager, @NonNull List<IN> transactions) {
        super(manager);
        orderQueue = new ConcurrentLinkedQueue<>();
        allTrans = new ConcurrentLinkedQueue<>();
        allTrans.addAll(transactions);
    }

    // center 通知 order 正在出块
    public void isOutBlock(boolean bool) {
        isOutBlock.set(bool);
    }
    
    // 是否发生阻塞
    public boolean isWaitting() {
        return waiting.get();
    }
    
    // 激活order的方法
    public abstract long activate();

    // 是否完成
    public boolean isFinish() {
        return !processing && orderQueue.isEmpty();
    }
    
    // 拉取产物
    public OUT pull() {
        return orderQueue.poll();
    }
    
    // 等待center进行出块
    protected void waitOutBlock() {
        while (isOutBlock.get()) {
            if (!waiting.get()) {
                waiting.set(true);
//                System.out.println("waiting block out...");
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("验签线程意外中断");
            }
        }
        if (waiting.get()) {
            waiting.set(false);
//            System.out.println("verifying trans start...");
        }
    } 

}
