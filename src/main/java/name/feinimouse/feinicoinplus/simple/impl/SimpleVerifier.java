package name.feinimouse.feinicoinplus.simple.impl;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
import name.feinimouse.feinicoinplus.exception.NodeRunningException;

import java.security.PrivateKey;

@SuppressWarnings("RedundantThrows")
public class SimpleVerifier extends Verifier {
    public SimpleVerifier(PrivateKey privateKey) {
        super(privateKey);
    }

    @Override
    protected void resolveGapPeriod() throws NodeRunningException {
        
    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {

    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        return null;
    }
}
