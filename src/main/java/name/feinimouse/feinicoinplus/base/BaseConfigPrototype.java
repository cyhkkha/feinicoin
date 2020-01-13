package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.BlockDao;
import name.feinimouse.feinicoinplus.core.ConsensusNetwork;
import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.node.CenterCore;
import name.feinimouse.feinicoinplus.core.node.NodeNetwork;
import name.feinimouse.feinicoinplus.core.node.VerifierCore;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseConfigPrototype {

    @Autowired
    protected AddressManager addressManager;
    @Autowired
    protected PublicKeyHub publicKeyHub;
    @Autowired
    protected SignGenerator signGenerator;
    @Autowired
    protected NodeNetwork nodeNetwork;
    @Autowired
    protected TransactionGenerator transactionGenerator;

    @Autowired
    protected CenterCore centerCore;
    @Autowired
    protected VerifierCore verifierCore;

    @Autowired
    protected ConsensusNetwork consensusNetwork;
    @Autowired
    protected BlockDao blockDao;
    
}
