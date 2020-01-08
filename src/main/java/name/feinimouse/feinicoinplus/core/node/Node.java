package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
import org.json.JSONObject;

public interface Node {
    String getAddress();
    void commit(Carrier carrier) throws BadCommitException;
    Carrier fetch(Carrier carrier) throws BadCommitException;
    void stopNode();
    boolean isStop();
    JSONObject nodeMsg();
    void start();
}
