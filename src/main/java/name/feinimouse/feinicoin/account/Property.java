package name.feinimouse.feinicoin.account;

import name.feinimouse.feinicoin.block.Hashable;

// 资产由中央节点进行写入
public class Property extends Account implements Hashable {
    // 资产名称
    private String name;
    // 过期时间
    private long expiration;
    // 资产持有账户
    private String owner;
    // 资产担保人的账户
    private String agent;
    // 签名
    private String signature;
    // 上一次改变引用的区块
    private long preBlockNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public long getPreBlockNum() {
        return preBlockNum;
    }

    @Override
    public void setPreBlockNum(long preBlockNum) {
        this.preBlockNum = preBlockNum;
    }
}