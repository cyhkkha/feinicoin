package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.NoSuchNodeException;
import name.feinimouse.feinicoinplus.core.exception.NodeBusyException;
import name.feinimouse.feinicoinplus.core.node.Node;

public interface NodeNetwork {
    String getAddress();
    void commit(Carrier carrier) throws NoSuchNodeException, NodeBusyException;
    Carrier fetch(Carrier carrier) throws NoSuchNodeException;
    void registerNode(Node node);
}
