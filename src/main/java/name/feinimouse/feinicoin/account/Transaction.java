package name.feinimouse.feinicoin.account;

import lombok.Data;
import name.feinimouse.feinicoin.block.Hashable;

// 交易信息由用户发起，并由检查节点节点进行审核
@Data
public class Transaction implements Hashable {
    // 交易的hash编号
    private String seriNum;
    // 交易时间戳
    private long timestamp;
    // 发起者
    private String sender;
    // 接收者
    private String receiver;
    // 金额
    private double coin;
    // 签名
    private String[] signature;
    
    // 交易的附加信息
    private TransExtFunc extFunc;
    
}