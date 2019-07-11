package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Sign;

public interface Header extends Hashable {
    // 区块编号
    long getNumber();
    // 上一个区块的哈希
    String getPreHash();
    // 时间戳
    long getTimestamp();
    // 中央节点区块生产者
    String getProducer();
    // 中央节点区块生产者签名
    Sign getSign();
    // 交易的默克尔树根
    String getTransRoot();
    // 账户的默克尔树根
    String getAccountRoot();
    // 资产默克尔树根
    String getAssetRoot();
    // 区块的版本号
    String getVersion();
}