package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import org.json.JSONObject;

public interface Node {
    void commit(Carrier carrier) throws BadCommitException;
    Carrier fetch(Carrier carrier) throws BadCommitException;
    void stopNode();
    boolean isStop();
    JSONObject nodeMsg();
    void start();
}
