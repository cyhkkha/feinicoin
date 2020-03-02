package name.feinimouse.feinicoinplus.consensus;

public interface BFTNet extends BFTConNode {
    void destroy();
    boolean isConsensus();
}