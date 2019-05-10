package name.feinimouse.feinicoin.account;

import name.feinimouse.feinicoin.block.Hashable;

// 账户信息每次由中央节点进行写入
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

    public void setHash(String hash) {
        this.hash = hash;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getPreBlockNum() {
        return preBlockNum;
    }

    public void setPreBlockNum(long preBlockNum) {
        this.preBlockNum = preBlockNum;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}