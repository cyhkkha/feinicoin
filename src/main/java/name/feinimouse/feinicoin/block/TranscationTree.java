package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Transcation;

public interface TranscationTree {
    public String getHeader();
    public void addTrans(Transcation t);
    public Transcation getTrans(String hash);
    public int searchTrans(String hash);
}