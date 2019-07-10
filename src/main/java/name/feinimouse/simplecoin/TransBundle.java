package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Hashable;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransBundle implements Hashable {
    
    private SimpleMerkelTree merkelTree;
    @Getter
    private JSONObject summaryJson;
    private Map<String, Integer> summary;
    @Getter
    private boolean hasChange = false;
    @Getter
    private long bundleTime = 0L;

    @Getter @Setter
    private SimpleSign sign;

    public TransBundle() {
        this.summaryJson = new JSONObject();
        this.merkelTree = new SimpleMerkelTree();
        this.summary = new ConcurrentHashMap<>();
    }
    
    public TransBundle(List<Transaction> ts) {
        this.summaryJson = new JSONObject();
        this.merkelTree = new SimpleMerkelTree(ts);
        this.hasChange = true;
        doBundle();
    }
    
    public void clear() {
        this.summaryJson = new JSONObject();
        this.merkelTree.clear();
        this.hasChange = true;
    }
    
    public void doBundle() {
        if (this.hasChange) {
            var before = System.nanoTime();
            summary.clear();
            merkelTree.getTransList().forEach(t -> {
                var sender = t.getSender();
                var receiver = t.getReceiver();
                var coin = (Integer)t.getCoin();
                summary.merge(sender, - coin, Integer::sum);
                summary.merge(receiver, coin, Integer::sum);
            });
//            for (Transaction t : merkelTree.getTransList()) {
//                var sender = t.getSender();
//                var receiver = t.getReceiver();
//                var coin = ((SimpleTransaction) t).getCoinInt();
//                var senderCoin = summaryJson.optInt(sender, 0);
//                var receiverCoin = summaryJson.optInt(receiver, 0);
//                summaryJson.put(sender, senderCoin - coin);
//                summaryJson.put(receiver, receiverCoin + coin);
//            }
            summaryJson = new JSONObject(summary);
            this.merkelTree.resetRoot();
            this.bundleTime = System.nanoTime() - before + merkelTree.getHashTime();
            this.hasChange=false;
        }
    }
    
    public void addTrans(@NonNull Transaction t) {
        merkelTree.addChild(t);
        this.hasChange = true;
    }
    
    @Override
    public String getHash() {
        return this.merkelTree.getRoot();
    }
}