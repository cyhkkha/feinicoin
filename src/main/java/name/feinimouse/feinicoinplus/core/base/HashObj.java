package name.feinimouse.feinicoinplus.core.base;


public abstract class HashObj <T> implements OrdinaryObj {
    protected String hash;

    public String gainHash() {
        return hash;
    }
    abstract public T obj();
}
