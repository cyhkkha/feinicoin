package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.NodeNetwork;
import name.feinimouse.feinicoinplus.exception.NoSuchNodeException;

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
