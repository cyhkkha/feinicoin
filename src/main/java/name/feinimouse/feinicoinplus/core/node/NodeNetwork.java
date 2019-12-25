package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.node.exce.NoSuchNodeException;

public interface NodeNetwork {
    String getAddress();
    void commit(Carrier carrier) throws NoSuchNodeException;
    Carrier fetch(Carrier carrier) throws NoSuchNodeException;
}
