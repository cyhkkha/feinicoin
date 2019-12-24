package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.node.exce.NoSuchNodeException;

public interface NodeNetwork {
    String getAddress();
    void commit(String address, Carrier carrier) throws NoSuchNodeException;
    Carrier fetch(String address, Carrier carrier) throws NoSuchNodeException;
}
