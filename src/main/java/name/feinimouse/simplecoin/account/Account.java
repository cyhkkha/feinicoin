package name.feinimouse.simplecoin.account;

import name.feinimouse.simplecoin.block.Hashable;

// 账户信息每次由中央节点进行写入
public interface Account extends Hashable {
    // 账户地址
    String getAddress();
    // 余额
    Number getCoin();
    // 公钥
    String getPublicKey();
    // 身份信息
    Identity getIdentity();
    // 额外字段
    ExtFunc getExtFunc();
    
}