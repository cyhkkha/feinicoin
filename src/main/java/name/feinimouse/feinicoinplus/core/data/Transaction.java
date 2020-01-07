package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;

@Data
public class Transaction implements BlockObj {
    private long timestamp;
    private String sender;
    private String receiver;
    private int number;
    private HashMap<String, String> exFunc;

    public Transaction(String sender, String receiver, int number) {
        this();
        this.sender = sender;
        this.receiver = receiver;
        this.number = number;
    }

    public Transaction() {
        timestamp = System.currentTimeMillis();
        exFunc = new HashMap<>();
    }
}
