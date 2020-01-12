package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.node.VerifierCore;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.RequestNotSupportException;

public class BaseVerifier extends Verifier {

    public BaseVerifier(VerifierCore verifierCore) {
        super(verifierCore);
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadRequestException {
        throw new RequestNotSupportException(this, carrier.getNetInfo(), "fetch");
    }
}
