package name.feinimouse.simplecoin.account;

import name.feinimouse.simplecoin.block.Hashable;

// 资产由中央节点进行写入
public interface Assets extends Hashable {
    // 资产类型
    String getType();
    // 资产摘要（编号）
    String getSummary();
    // 时间戳
    long getTimestamp();
    // 所有者
    String getOwner();
    // 发行者
    String getIssuer();
    // 签名
    Sign getSign();
    // 状态
    String getStatus();
    // 关联的交易历史
    History getHistory();
    // 价值（数量）
    Number getCoin();
    // 额外字段
    ExtFunc getExtFunc();
}