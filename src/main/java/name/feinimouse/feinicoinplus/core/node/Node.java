package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
import org.json.JSONObject;

public interface Node {
    int NODE_ORDER = 200;
    int NODE_VERIFIER = 201;
    int NODE_CENTER = 202;
    int NODE_ENTER = 203;

    int MSG_COMMIT_ORDER = 100;
    int MSG_COMMIT_VERIFIER = 101;
    int MSG_CALLBACK_VERIFIER = 102;
    int MSG_FETCH_ORDER = 103;
    int MSG_CALLBACK_ORDER = 104;
    
    String getAddress();
    int getNodeType();
    void commit(Carrier carrier) throws BadCommitException;
    Carrier fetch(Carrier carrier) throws BadCommitException;
    void stopNode();
    boolean isStop();
    JSONObject nodeMsg();
    void start();
}
