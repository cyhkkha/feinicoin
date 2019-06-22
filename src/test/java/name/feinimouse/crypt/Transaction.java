package name.feinimouse.crypt;

import lombok.Data;
import name.feinimouse.feinicoin.account.TransExtFunc;
import name.feinimouse.feinicoin.block.Hashable;
import org.json.JSONObject;

// 交易信息由用户发起，并由检查节点节点进行审核
@Data
public class Transaction implements Hashable {
    // 交易时间戳
    private long timestamp;
    // 发起者
    private String sender;
    // 接收者
    private String receiver;
    // 金额
    private int coin;
    // 签名
    private SM2.Signature signature;
    
    private String summary;
    public Transaction() {}

    public Transaction(
        String sender, String receiver,
        int coin, long timestamp
    ) {
        this.sender = sender;
        this.receiver = receiver;
        this.coin = coin;
        this.timestamp = timestamp;
        this.summary = new JSONObject()
            .put("sender", sender)
            .put("receiver", receiver)
            .put("coin", coin)
            .put("timestamp", timestamp)
            .toString();
    }
    
}