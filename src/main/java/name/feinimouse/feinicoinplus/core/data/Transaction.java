package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;

@Data
public class Transaction implements BlockObj {
    private long timestamp;
    private String sender;
    private String receiver;
    private int coin;
    private HashMap<String, String> exFunc;

    public Transaction(String sender, String receiver, int coin) {
        this();
        this.sender = sender;
        this.receiver = receiver;
        this.coin = coin;
    }

    public Transaction() {
        timestamp = System.currentTimeMillis();
        exFunc = new HashMap<>();
    }
    
    @Override
    public String toString() {
        return String.format("[%d](%s -> %s)", coin, sender, receiver);
    }
}
