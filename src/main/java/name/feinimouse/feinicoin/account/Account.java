package name.feinimouse.feinicoin.account;

import lombok.Data;
import name.feinimouse.feinicoin.block.Hashable;

// 账户信息每次由中央节点进行写入
@Data
public class Account implements Hashable {
    // 账户的hash地址
    private String address;
    // 账户的数字货币
    private double coin;
    // 账户的公钥
    private String key;
    // 持有者的身份
    private Identity identity;
    
    private AccountExtFunc extFunc;
    // 账户是否发生过改变
    // private boolean changed;
    
}