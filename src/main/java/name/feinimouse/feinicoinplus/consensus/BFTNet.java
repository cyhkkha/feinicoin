package name.feinimouse.feinicoinplus.consensus;

public interface BFTNet extends BFTNode {
    void destroy();
    boolean isConsensus();
}