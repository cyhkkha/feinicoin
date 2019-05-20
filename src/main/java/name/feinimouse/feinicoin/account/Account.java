package name.feinimouse.feinicoin.account;

import lombok.Data;
import name.feinimouse.feinicoin.block.Hashable;

// 账户信息每次由中央节点进行写入
@Data
public class Account implements Hashable {
    // 账户的hash
    private String hash;
    // 账户的数字货币
    private double coin;
    // 账户的公钥
    private String key;
    // 上一次改变引用的区块
    private long preBlockNum;
    // 账户是否发生过改变
    private boolean changed;

    @Override
    public String getHash() {
        return this.hash;
    }
    
}