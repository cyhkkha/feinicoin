package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.ConsensusNetwork;
import name.feinimouse.feinicoinplus.core.node.FetchCenter;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.node.exception.RequestNotSupportException;

public class BaseCenter extends FetchCenter {

    public BaseCenter(CenterContext centerContext, HashGenerator hashGen, ConsensusNetwork consensusNetwork) {
        setCenterContext(centerContext);
        setHashGen(hashGen);
        setConsensusNetwork(consensusNetwork);
    }
    
    @Override
    protected void resolveCommit(Carrier carrier) throws BadRequestException {
        throw new RequestNotSupportException(this, carrier.getNetInfo(), "commit");
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadRequestException {
        throw new RequestNotSupportException(this, carrier.getNetInfo(), "fetch");
    }
    
}
