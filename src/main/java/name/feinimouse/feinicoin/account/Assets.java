package name.feinimouse.feinicoin.account;

import lombok.Data;
import name.feinimouse.feinicoin.block.Hashable;

// 资产由中央节点进行写入
@Data
public class Assets implements Hashable {
    // 资产类型
    private String type;
    // 资产的编号
    private String seriNum;
    // 资产建立的时间戳
    private long timestamp;
    // 过期时间 移至智能合约中实现
    // private long expiration;
    // 可操作资产的所有者
    private String owner;
    // 资产的发布者
    private String issuer;
    // 资产建立签名
    private String[] signature;
    // 状态：如已完成、过期等
    private String status;
    
    // 可用coin资本
    private double coin;

    // 账户是否发生过改变
    // private boolean changed;
    // 资产的附加信息，如智能合约等
    private PropertyExtFunc extFunc;
    
}