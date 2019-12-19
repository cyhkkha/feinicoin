package name.feinimouse.feinicoinplus.core.obj;


public abstract class HashObj implements OrdinaryObj {
    protected String hash;

    public String getHash() {
        return hash;
    }
    abstract public Object getObject();
}
