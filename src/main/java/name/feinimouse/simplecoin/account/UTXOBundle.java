package name.feinimouse.simplecoin.account;

import lombok.Getter;
import lombok.Setter;
import net.openhft.hashing.LongHashFunction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class UTXOBundle extends ConcurrentLinkedQueue<Transaction> implements Assets {
    
    @Getter @Setter
    private String type;
    private LongHashFunction xx;
    
    public UTXOBundle() {
        super();
        xx = LongHashFunction.xx();
    }

    @Override
    public String getSummary() {
         var array = new JSONArray(this.stream()
            .map(Transaction::getHash)
            .collect(Collectors.toList()));
         var json = new JSONObject().put("timestamp", timestamp)
             .put("owner", owner).put("issuer", issuer)
             .put("coin", coin).put("history", array)
             .put("hash", getHash());
         return json.toString();
    }

    @Override
    public String getHash() {
        var totalHash = this.stream().map(Transaction::getHash)
            .map(Long::valueOf).reduce(Long::sum).orElse(0L);
        return String.valueOf(xx.hashLong(totalHash));
    }

    @Getter @Setter
    private long timestamp;

    @Getter @Setter
    private String owner;

    @Getter @Setter
    private String issuer;
    
    private int coin = 0;

    @Override
    public boolean add(Transaction transaction) {
        this.coin += (Integer) transaction.getCoin();
        return super.add(transaction);
    }

    @Override
    public Number getCoin() {
        return coin;
    }

    @Override
    public Sign getSign() {
        return null;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public History getHistory() {
        return null;
    }

    @Override
    public ExtFunc getExtFunc() {
        return null;
    }
}
