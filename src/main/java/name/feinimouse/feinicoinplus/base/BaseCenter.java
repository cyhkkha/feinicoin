package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.ConsensusNetwork;
import name.feinimouse.feinicoinplus.exception.BadCommitException;

public class BaseCenter extends Center {

    public BaseCenter(CenterContext centerContext, HashGenerator hashGen, ConsensusNetwork consensusNetwork) {
        super(centerContext, hashGen, consensusNetwork);
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "commit");
    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "fetch");
    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "commit");

    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "fetch");
    }

    @Override
    protected void resolveGapPeriod() {
    }
}
