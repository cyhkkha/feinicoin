package name.feinimouse.feinicoin.block;

public abstract class Block {
    protected String hash;
    protected String preHash;
    protected long timestamp;
    protected String producer;
    protected String transRoot;
    protected String accountRoot;
    protected String propertyRoot;
}