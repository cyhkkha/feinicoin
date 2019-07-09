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
    private enum Model{ BCBDC, UTXO, Account, BCBDC_Mix }
    @Setter @Getter
    private Model model;
    
    private SimpleOrder order;
    private UserManager manager;
    
    @Getter
    private boolean running = false;
    
    @Getter @Setter
    private String name;
    // 出块时间限制，默认1s
    @Getter @Setter
    private long outTime = 1000L * 1000000L;
    @Getter
    // 运行开始时间
    private long startTime = 0L;
    @Getter
    // 总运行时间
    private long runTime = 0L;
    
    public SimpleCenter(SimpleOrder order, Model model) {
        this.order = order;
        this.manager = order.getUserManager();
        this.model = model;
    }
    
    @Override
    public void activate() {
        running = true;
        startTime = System.nanoTime();
        switch (model) {
            case UTXO: runUTXO();
                break;
            case BCBDC: runBCBDC();
                break;
            case Account: runAccount();
                break;
            case BCBDC_Mix: runMix();
                break;
             default: stop();
                 throw new RuntimeException("No Running Model !!");
        }
        stop();
    }
    
    private void stop() {
        if (running) {
            runTime = System.nanoTime() - startTime;
            running = false;
        }
    }
    
    private void runUTXO() {}
    private void runBCBDC() {}
    private void runAccount() {}
    private void runMix() {}

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
