package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.ConsensusNetwork;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
import name.feinimouse.feinicoinplus.exception.NodeRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("center")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleCenter extends Center {

    @Autowired
    public SimpleCenter(CenterContext content, HashGenerator hashGen, ConsensusNetwork consensusNetwork) {
        super(content, hashGen, consensusNetwork);
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
