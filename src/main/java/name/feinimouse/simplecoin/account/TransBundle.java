package name.feinimouse.simplecoin.account;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.simplecoin.block.Hashable;
import name.feinimouse.simplecoin.block.SimpleMerkelTree;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransBundle implements Hashable {
    @Getter
    private SimpleMerkelTree<Transaction> merkelTree;
    @Getter
    private Map<String, Integer> summary;
    @Getter
    private boolean hasChange = false;
    @Getter
    private long bundleTime = 0L;

    @Getter @Setter
    private SimpleSign sign;

    public TransBundle() {
        this.merkelTree = new SimpleMerkelTree<>();
        this.summary = new ConcurrentHashMap<>();
    }
    
    public TransBundle(List<Transaction> ts) {
        this.merkelTree = new SimpleMerkelTree<>(ts);
        this.summary = new ConcurrentHashMap<>();
        this.hasChange = true;
        doBundle();
    }
    
    public void clear() {
        this.summary.clear();
        this.merkelTree.clear();
        this.hasChange = true;
    }
    
    public void doBundle() {
        if (this.hasChange) {
            var before = System.nanoTime();
            summary.clear();
            merkelTree.getList().forEach(t -> {
                var sender = t.getSender();
                var receiver = t.getReceiver();
                var coin = (Integer)t.getCoin();
                summary.merge(sender, - coin, Integer::sum);
                summary.merge(receiver, coin, Integer::sum);
            });
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