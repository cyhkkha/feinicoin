package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Center;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: SimpleCenter
 * Program : feinicoin
 * Description :
 */
public class SimpleCenter implements Center {
    private SimpleOrder order;
    private UserManager manager;
    
    @Getter
    private boolean hasRunning = false;
    
    @Getter
    private String name = "simple center";
    // 出块时间限制，默认1s
    @Getter @Setter
    private long outTime = 1000L * 1000000L;
    @Getter
    // 运行开始时间
    private long startTime = 0L;
    @Getter
    // 总运行时间
    private long runTime = 0L;
    
    public SimpleCenter(UserManager manager, SimpleOrder order) {
        this.order = order;
        this.manager = manager;
    }
    
    @Override
    public void activate() {
        hasRunning = true;
        startTime = System.nanoTime();
    }
    
    private void stop() {
        hasRunning = false;
    }

    @Override
    public Block createBlock() {
        return null;
    }

    @Override
    public void write() {}

    @Override
    public void broadcast() {}

    @Override
    public void syncBlock(Block b) {}
    
}
