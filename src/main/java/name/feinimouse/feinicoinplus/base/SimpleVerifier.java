package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.exception.BadCommitException;

public class SimpleVerifier extends Verifier {

    public SimpleVerifier(PublicKeyHub publicKeyHub, SignGenerator signGen) {
        super(publicKeyHub, signGen);
    }

    @Override
    protected void resolveGapPeriod() {
        
    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "fetch");
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        throw BadCommitException.requestNotSupport(this, "fetch");
    }
}
