package name.feinimouse.feinicoin.account;

import lombok.Data;
import name.feinimouse.feinicoin.block.Hashable;

// 交易信息由用户发起，并由检查节点节点进行审核
@Data
public class Transaction implements Hashable {
    // 交易的hash编号
    private String hash;
    // 交易时间戳
    private long timestamp;
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
    private long preBlockNum;
    
    // 交易的附加信息
    private ExtFunc extFunc;

    @Override
    public String getHash() {
        return hash;
    }
}