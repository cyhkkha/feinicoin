package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;

public interface Node {
    String NODE_ORDER = "ORDER";
    String NODE_VERIFIER = "VERIFIER";
    String NODE_CENTER = "CENTER";
    String NODE_ENTER = "ENTER";
    String NODE_CENTER_CLASSICAL = "CLASSICAL";

    String MSG_COMMIT_ORDER = "COMMIT_ORDER";
    String MSG_COMMIT_VERIFIER = "COMMIT_VERIFIER";
    String MSG_CALLBACK_VERIFIER = "CALLBACK_VERIFIER";
    String MSG_FETCH_ORDER = "FETCH_ORDER";
    String MSG_CALLBACK_ORDER = "CALLBACK_ORDER";
    
    String getAddress();
    String getNodeType();
    void commit(Carrier carrier) throws BadRequestException;
    Carrier fetch(Carrier carrier) throws BadRequestException;
    void stopNode();
    boolean isStop();
    void start();
}
