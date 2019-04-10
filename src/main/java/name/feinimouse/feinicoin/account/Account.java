package name.feinimouse.feinicoin.account;

public abstract class Account {
    protected String hash;
    protected double coin;
    protected String publicKey;

    public abstract int sendCoin(String key, double coin);
    public abstract int saveCoin(double coin);
}