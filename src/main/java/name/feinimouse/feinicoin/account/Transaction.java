package name.feinimouse.feinicoin.account;

import name.feinimouse.feinicoin.block.Hashable;

// 交易信息由用户发起，并由检查节点节点进行审核
public class Transaction implements Hashable {
    // 交易的hash编号
    private String hash;
    // 交易的过期时间
    private long expiration;
    // 发起者
    private String sender;
    // 接收者
    private String receiver;
    // 金额
    private double coin;
    // 签名
    private String signature;
    /**
     * 最近的一个引用的区块信息，为防止交易在分叉上被重复广播
     * 声明该笔交易在某个分叉上生效
     */
    private String preBlock;
    private long preBlockNum;

    @Override
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPreBlock() {
        return preBlock;
    }

    public void setPreBlock(String preBlock) {
        this.preBlock = preBlock;
    }

    public long getPreBlockNum() {
        return preBlockNum;
    }

    public void setPreBlockNum(long preBlockNum) {
        this.preBlockNum = preBlockNum;
    }
}