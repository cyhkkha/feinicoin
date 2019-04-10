package name.feinimouse.feinicoin.account;

// 资产由中央节点进行写入
public abstract class Property extends Account {
    // 资产名称
    protected String name;
    // 过期时间
    protected long expiration;

    // 资产持有账户
    protected String owner;
    // 资产担保人的账户
    protected String agent;
    // 签名
    protected String signature;
    // 上一次改变引用的区块
    protected long preBlockNum;
}