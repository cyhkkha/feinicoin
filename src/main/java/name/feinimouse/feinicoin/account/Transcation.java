package name.feinimouse.feinicoin.account;

public abstract class Transcation {

    protected String hash;
    protected String Sender;
    protected String receiver;
    protected double coin;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transcation) {
            return ((Transcation) obj).getHash() == this.hash;
        }
        return false;
    }

    public String getHash() {
        return hash;
    }

}