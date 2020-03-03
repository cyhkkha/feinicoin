package name.feinimouse.feinicoinplus.consensus;

public interface BFTNode {
    
    String STAGE_STOP = "STAGE_STOP";
    String STAGE_PREPARE = "STAGE_PRE_PREPARE";
    String STAGE_COMMIT = "STAGE_PREPARE";
    String STAGE_REPLY = "STAGE_COMMIT";

    String ADDRESS_NET = "0000_0000_0000_0000";
    
    String getAddress();
    
    void start(BFTMessage bftMessage);
    
    void prePrepare(BFTMessage bftMessage);

    void prepare(BFTMessage bftMessage);

    void commit(BFTMessage bftMessage);
    
    void reply();
}