package name.feinimouse.feinicoinplus.simple;

import name.feinimouse.feinicoinplus.core.NodeNetwork;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.NoSuchNodeException;

// TODO
public class MapNodeNetwork implements NodeNetwork {
    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public void commit(Carrier carrier) throws NoSuchNodeException {

    }

    @Override
    public Carrier fetch(Carrier carrier) throws NoSuchNodeException {
        return null;
    }
}
