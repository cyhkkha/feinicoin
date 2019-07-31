package name.feinimouse.simplecoin.block;

import lombok.Getter;
import lombok.NonNull;
import name.feinimouse.feinicoin.block.Hashable;
import name.feinimouse.feinicoin.block.MerkelTree;
import net.openhft.hashing.LongHashFunction;

import java.util.LinkedList;
import java.util.List;

public class SimpleMerkelTree <T extends Hashable> implements MerkelTree<T> {
    
    private LongHashFunction xxHash;
    private boolean hasChange = false;
    @Getter
    private List<T> list;
    @Getter
    private long hashTime = 0L;
    @Getter
    private String root;
    
    public SimpleMerkelTree() {
        this.list = new LinkedList<>();
        this.xxHash = LongHashFunction.xx();
    }
    
    public SimpleMerkelTree(@NonNull List<T> ts) {
        this.xxHash = LongHashFunction.xx();
        this.list = ts;
        this.hasChange = true;
    }
    
    @Override
    public void addChild(T child) {
        this.list.add(child);
        this.hasChange = true;
    }

    @Override
    public void resetRoot() {
        if (hasChange) {
            var before = System.nanoTime();
            var hashCount = list.stream()
                .map(t -> Long.valueOf(t.getHash()))
                .reduce(Long::sum).orElse(0L);
            this.root = String.valueOf(xxHash.hashLong(hashCount));
            this.hashTime = System.nanoTime() - before;
            this.hasChange = false;
        }
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public void clear() {
        this.list.clear();
        this.hasChange = true;
    }

    @Override
    public String getHash() {
        return this.getRoot();
    }
}
