package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.NoSuchNodeException;

public interface NodeNetwork {
    String getAddress();
    void commit(Carrier carrier) throws NoSuchNodeException;
    Carrier fetch(Carrier carrier) throws NoSuchNodeException;
}
