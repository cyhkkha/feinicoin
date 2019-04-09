package name.feinimouse.feinicoin.block;

import java.util.List;

public abstract class BlockBody {
    protected List<Transcation> tranList;
    protected List<String> merkleTree;
    public void addTrans(Transcation t) {
        this.tranList.add(t);
        this.resetMerkle();
    }
    public int size() {
        return tranList.size();
    }
    public String getHeader() {
        return merkleTree.get(0);
    }

    public Transcation geTranscationByIndex(int i) {
        return tranList.get(i);
    }

    abstract public int hasTranscation(String hash);
    abstract public Transcation getTranscation(String hash);
    abstract protected void resetMerkle();
}