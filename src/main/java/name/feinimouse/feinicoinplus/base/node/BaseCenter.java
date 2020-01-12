package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.CenterCore;
import name.feinimouse.feinicoinplus.core.node.FetchCenter;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.RequestNotSupportException;

public class BaseCenter extends FetchCenter {

    public BaseCenter(CenterCore centerCore) {
        super(centerCore);
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
