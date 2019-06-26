package name.feinimouse.testcoin;

import lombok.Data;
import name.feinimouse.feinicoin.account.ExtFunc;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.feinicoin.account.Transaction;
import org.json.JSONObject;

// 交易信息由用户发起，并由检查节点节点进行审核
@Data
public class TransactionTest implements Transaction {
    // 交易时间戳
    private long timestamp;
    // 发起者
    private String sender;
    // 接收者
    private String receiver;
    // 金额
    private Integer coin;
    // 签名
    private SignTest sign;

    private String summary;

    public TransactionTest() {
    }

    public TransactionTest(String sender, String receiver, int coin, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.coin = coin;
        this.timestamp = timestamp;
        resetSummary();
    }

    public String resetSummary() {
        var summary = new JSONObject().put("sender", sender).put("receiver", receiver).put("coin", coin)
                .put("timestamp", timestamp).toString();
        this.summary = summary;
        return summary;
    }

    @Override
    public ExtFunc getExtFunc() {
        return null;
    }

    @Override
    public void sign(Sign sign) {
        this.setSign((SignTest)sign);
    }
    
}