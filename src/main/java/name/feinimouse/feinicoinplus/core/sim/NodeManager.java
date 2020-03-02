package name.feinimouse.feinicoinplus.core.sim;

import name.feinimouse.feinicoinplus.core.node.ClassicalCenter;
import name.feinimouse.feinicoinplus.core.node.FetchCenter;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;

public interface NodeManager {
    Order getOrder();

    Verifier getVerifier();

    FetchCenter getFetchCenter();

    ClassicalCenter getClassicalCenter();

    void startFetchNode();

    void startClassicalNode();
    
    void waitAndDestroy();
}
