package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.node.CenterCore;
import name.feinimouse.feinicoinplus.core.node.NodeNetwork;
import name.feinimouse.feinicoinplus.core.node.VerifierCore;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfigPrototype {
    @Value("${NODE_INTERVAL}")
    protected long NODE_INTERVAL;

    @Value("${FETCH_INTERVAL}")
    protected long FETCH_INTERVAL;

    protected AddressManager addressManager;
    protected PublicKeyHub publicKeyHub;
    protected SignGenerator signGenerator;
    protected NodeNetwork nodeNetwork;
    protected TransactionGenerator transactionGenerator;

    protected CenterCore centerCore;
    protected VerifierCore verifierCore;


    @Autowired
    public void setCenterCore(CenterCore centerCore) {
        this.centerCore = centerCore;
    }

    @Autowired
    public void setVerifierCore(VerifierCore verifierCore) {
        this.verifierCore = verifierCore;
    }

    @Autowired
    public void setSignGenerator(SignGenerator signGenerator) {
        this.signGenerator = signGenerator;
    }

    @Autowired
    public void setPublicKeyHub(PublicKeyHub publicKeyHub) {
        this.publicKeyHub = publicKeyHub;
    }

    @Autowired
    public void setNodeNetwork(NodeNetwork nodeNetwork) {
        this.nodeNetwork = nodeNetwork;
    }

    @Autowired
    public void setAddressManager(AddressManager addressManager) {
        this.addressManager = addressManager;
    }

    @Autowired
    public void setTransactionGenerator(TransactionGenerator transactionGenerator) {
        this.transactionGenerator = transactionGenerator;
    }
}
