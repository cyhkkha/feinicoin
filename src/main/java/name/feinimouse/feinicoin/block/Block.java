package name.feinimouse.feinicoin.block;

public abstract class Block {
    // 区块的哈希
    protected String hash;
    // 上一个区块的哈希
    protected String preHash;
    // 时间戳
    protected long timestamp;
    // 中央节点区块生产者
    protected String producer;
    // 中央节点区块生产者签名
    protected String signature;
    // 交易树默克尔树根
    protected String transRoot;
    // 账户的默克尔树根
    protected String accountRoot;
    // 资产的默克尔树根
    protected String propertyRoot;
    // 区块的版本号
    protected String version;
}