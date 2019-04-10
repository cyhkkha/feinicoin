package name.feinimouse.feinicoin.account;

// 交易信息由用户发起，并由检查节点节点进行审核
public abstract class Transcation {
    // 交易的hash编号
    protected String hash;
    // 交易的过期时间
    protected long expiration;
    // 发起者
    protected String sender;
    // 接收者
    protected String receiver;
    // 金额
    protected double coin;
    // 签名
    protected String signature;
    /**
     * 最近的一个引用的区块信息，为防止交易在分叉上被重复广播
     * 声明该笔交易在某个分叉上生效
     */
    protected String preBlock;
    protected long preBlockNum;
    
}