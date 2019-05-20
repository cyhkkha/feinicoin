package name.feinimouse.feinicoin.account;

import lombok.Data;
import name.feinimouse.feinicoin.block.Hashable;

// 资产由中央节点进行写入
@Data
public class Property implements Hashable {
    // 资产名称
    private String name;
    // 资产建立的时间戳
    private long timestamp;
    // 过期时间
    private long expiration;
    // 资产持有账户
    private String[] owners;
    // 资产担保人的账户
    private String[] agents;
    // 资产建立签名
    private String signature;

    // 账户的hash
    private String hash;
    // 原始资本
    private double capital;
    // 账户的当前的价值
    private double currentValue;
    // 上一次改变引用的区块
    private long preBlockNum;
    // 账户是否发生过改变
    private boolean changed;
    // 资产的附加信息，如智能合约等
    private ExtFunc extFunc;

    @Override
    public String getHash() {
        return hash;
    }
}