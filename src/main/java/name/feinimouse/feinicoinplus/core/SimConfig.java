package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.*;

public interface SimConfig {
    AddressManager addressManager();
    SignGenerator signGenerator();
    HashGenerator hashGenerator();
    CenterDao centerDao();
    PublicKeyHub publicKeyHub();
    AccountManager accountManager();
    AssetManager assetManager();
    CenterContext centerContext();
    ConsensusNetwork consensusNetwork();
    TransactionGenerator transactionGenerator();
    NodeNetwork nodeNetWork();
    Order order();
    Verifier verifier();
    Center center();
    SimRunner simRunner();
}
