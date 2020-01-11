package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.exception.NoSuchNodeException;
import name.feinimouse.feinicoinplus.core.node.exception.NodeBusyException;

public interface NodeNetwork {
    String getAddress();
    void commit(Carrier carrier) throws NoSuchNodeException, NodeBusyException;
    Carrier fetch(Carrier carrier) throws NoSuchNodeException;
    void registerNode(Node node);
}
