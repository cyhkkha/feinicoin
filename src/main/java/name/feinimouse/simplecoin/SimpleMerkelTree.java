package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.MerkelTree;
import net.openhft.hashing.LongHashFunction;

import java.util.ArrayList;
import java.util.List;

public class SimpleMerkelTree implements MerkelTree<Transaction> {
    
    private LongHashFunction xxHash;
    private boolean hasChange = false;
    @Getter
    private List<Transaction> transList;
    @Getter
    private long hashTime = 0L;
    @Getter
    private String root;
    
    public SimpleMerkelTree() {
        this.transList = new ArrayList<>();
        this.xxHash = LongHashFunction.xx();
    }
    
    public SimpleMerkelTree(@NonNull List<Transaction> ts) {
        this();
        this.transList = ts;
        this.hasChange = true;
    }
    
    @Override
    public void addChild(Transaction trans) {
        this.transList.add(trans);
        this.hasChange = true;
    }
    
    @Override
    public void resetRoot() {
        if (hasChange) {
            var before = System.nanoTime();
            var hashCount = transList.stream()
                .map(trans -> Long.valueOf(trans.getHash()))
                .reduce((count, hash) -> count + hash).orElse(0L);
            this.root = String.valueOf(xxHash.hashLong(hashCount));
            this.hashTime = System.nanoTime() - before;
            this.hasChange = false;
        }
    }

    @Override
    public int size() {
        return this.transList.size();
    }

    @Override
    public void clear() {
        this.transList.clear();
        this.hasChange = true;
    }
}
