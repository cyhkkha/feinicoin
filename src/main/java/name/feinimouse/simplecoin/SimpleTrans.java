package name.feinimouse.simplecoin;

import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.account.ExtFunc;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.feinicoin.account.Transaction;

public class SimpleTrans implements Transaction {

    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String sender;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private Number coin;
    @Getter @Setter
    private Sign sign;
    @Getter
    private String summary;

    public SimpleTrans(long timestamp, String sender, String receiver, Number coin) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.coin = coin;
        resetSummary();
    }

    public String resetSummary() {
        JSONObject json = new JSONObject();
        summary = json.put("timestamp", timestamp)
            .put("sender", sender)
            .put("receiver", receiver)
            .put("coin", coin)
            .toString();
        return summary;
    }

    @Override
    public ExtFunc getExtFunc() {
        return null;
    }

    @Override
    public void sign(Sign sign) {
        this.setSign(sign);
    }

}