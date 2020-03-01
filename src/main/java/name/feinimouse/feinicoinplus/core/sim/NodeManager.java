package name.feinimouse.feinicoinplus.core.sim;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.lambda.ReturnRunner;

public interface NodeManager {
    Order getOrder();
    Verifier getVerifier();
    FetchCenter getFetchCenter();
    ClassicalCenter getClassicalCenter();
    long commitTrans(int count, Node node, ReturnRunner<Carrier> generator);
    void startFetchNode();
    void startClassicalNode();
}
