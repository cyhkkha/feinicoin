package name.feinimouse.simplecoin;

import lombok.Getter;
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
    @Getter
    private String name = "simple center";
    
    public SimpleCenter(TransBundle tb) {
        
    }

    @Override
    public void activate() {}

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
