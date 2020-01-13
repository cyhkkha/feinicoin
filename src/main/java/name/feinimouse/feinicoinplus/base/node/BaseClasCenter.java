package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.RequestNotSupportException;
import name.feinimouse.feinicoinplus.core.node.CenterCore;
import name.feinimouse.feinicoinplus.core.node.ClassicalCenter;
import name.feinimouse.feinicoinplus.core.node.VerifierCore;

public class BaseClasCenter extends ClassicalCenter {
    
    public BaseClasCenter(CenterCore centerCore, VerifierCore verifierCore) {
        super(centerCore, verifierCore);
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadRequestException {
        throw new RequestNotSupportException(this, carrier.getNetInfo(), "fetch");
    }
}
