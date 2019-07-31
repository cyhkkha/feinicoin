package name.feinimouse.simplecoin.account;

import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.account.ExtFunc;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.feinicoin.account.Transaction;

public class SimpleTransaction implements Transaction {

    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private String sender;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private Sign sign;
    @Getter @Setter
    private String hash;
    @Getter
    private String summary;
    private int coin;

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public Number getCoin() {
        return this.coin;
    }

    public int getCoinInt() {
        return this.coin;
    }

    public SimpleTransaction(long timestamp, String sender, String receiver, int coin) {
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
}