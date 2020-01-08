package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.core.sim.TransactionGenerator;

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
    Order order();
    Verifier verifier();
    Center center();
    SimRunner simRunner();
}
