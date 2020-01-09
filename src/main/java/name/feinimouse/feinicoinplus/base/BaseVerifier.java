package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.node.exception.RequestNotSupportException;

public class BaseVerifier extends Verifier {

    public BaseVerifier(PublicKeyHub publicKeyHub, SignGenerator signGen) {
        super(publicKeyHub, signGen);
    }
    
    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadRequestException {
        throw new RequestNotSupportException(this, carrier.getNetInfo(), "fetch");
    }
}
