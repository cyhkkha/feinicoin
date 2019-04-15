package name.feinimouse.feinicoin.block;

public class Header {
    // 区块的哈希
    private String hash;
    // 上一个区块的哈希
    private String preHash;
    // 时间戳
    private long timestamp;
    // 中央节点区块生产者
    private String producer;
    // 中央节点区块生产者签名
    private String signature;
    // 交易树默克尔树根
    private String transRoot;
    // 账户的默克尔树根
    private String accountRoot;
    // 资产的默克尔树根
    private String propertyRoot;
    // 区块的版本号
    private String version;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTransRoot() {
        return transRoot;
    }

    public void setTransRoot(String transRoot) {
        this.transRoot = transRoot;
    }

    public String getAccountRoot() {
        return accountRoot;
    }

    public void setAccountRoot(String accountRoot) {
        this.accountRoot = accountRoot;
    }

    public String getPropertyRoot() {
        return propertyRoot;
    }

    public void setPropertyRoot(String propertyRoot) {
        this.propertyRoot = propertyRoot;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}