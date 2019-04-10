package name.feinimouse.feinicoin.account;

public abstract class Property extends Account {
    protected String account;
    protected String agent;

    public abstract int drawProperty();
    public abstract int recovery();
}