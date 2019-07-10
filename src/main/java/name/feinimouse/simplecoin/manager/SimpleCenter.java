package name.feinimouse.simplecoin.manager;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Center;
import name.feinimouse.simplecoin.UserManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: SimpleCenter
 * Program : feinicoin
 * Description :
 */
public abstract class SimpleCenter implements Center {
    protected SimpleOrder order;
    protected UserManager manager;
    
    // 账户缓存
    protected Map<String, Integer> blockAccountMap;
    // 交易缓存
    protected List<String> bolckTransactionList;
    
    @Getter @Setter
    protected String name;
    
    // 出块时间限制，默认1s
    @Getter @Setter
    protected long outBlockTime = 1000L;
    
    // 数据库存储时间
    @Getter
    protected List<Long> saveTimes;
    
    // 运行开始时间
    private long startTime = 0L;
    // 总运行时间
    @Getter
    private long runTime = 0L;
    
    @Getter
    private boolean running = false;
    
    public SimpleCenter(@NonNull SimpleOrder order) {
        this.order = order;
        this.manager = order.getUserManager();
        this.blockAccountMap = new ConcurrentHashMap<>();
        this.bolckTransactionList = new LinkedList<>();
    }
    
    public void stop() {
        if (running) {
            running = false;
        }
        if (startTime > 0L) {
            runTime = System.nanoTime() - startTime;
            startTime = 0L;
        }
    }
    
    protected abstract void collectTransaction();
    
    @Override
    public void activate() {
        // 多线程执行器
        var executor = Executors.newFixedThreadPool(2);
        // 该线程池中每个线程都维护自己的任务队列。当自己的任务队列执行完成时，会帮助其他线程执行其中的任务。主动找活干
        // Executors.newWorkStealingPool(2);
        // 执行order的验证和排序
        var orderRes = executor.submit(order::activate);
        var centerRes = executor.submit(() -> {
            // 先让order初始化
            Thread.yield();
            while (!order.isFinish()) {
                // 从order收集交易
                collectTransaction();
                // 统计写入时间
                var saveTimeStart = System.nanoTime();
                // 出块
                var block = createBlock();
                // 写入数据库
                write(block);
                saveTimes.add(System.nanoTime() - saveTimeStart);
            }
        });
        try {
            // TODO 这里要谨慎重写
            orderRes.get();
            centerRes.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }
    
    @Override
    public void broadcast() {}

    @Override
    public void syncBlock(Block b) {}
    
}
