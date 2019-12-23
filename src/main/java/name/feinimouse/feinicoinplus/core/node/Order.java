package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import name.feinimouse.feinicoinplus.core.node.exce.NodeRunningException;
import org.json.JSONObject;

// Order基类
public class Order extends CacheNode {
    
    private SignAttachCMContainer fetchWait;
    
    public Order(String nodeType, Class<?>[] supportClass) {
        super(nodeType, supportClass);
        fetchWait = new SignAttachCMContainer(supportClass);
    }

    @Override
    public <T> SignAttachObj<T> fetch(JSONObject json, Class<T> tClass) throws BadCommitException {
        if (isStop()) {
            throw new BadCommitException("Node: " + nodeType + " is not working");
        }
        String origin = json.getString("origin");
        if (!origin.toLowerCase().equals("center")) {
            throw new BadCommitException("Can't resolve origin of " + origin);
        }
        return fetchWait.get(tClass);
    }

    @Override
    public JSONObject fetch(JSONObject json) throws BadCommitException {
        return null;
    }

    @Override
    protected void afterWork() {
        fetchWait.clear();
        super.afterWork();
    }

    @Override
    protected boolean resolveMassage(JSONObject json) {
        return false;
    }

    @Override
    protected boolean resolveWait() {
        return false;
    }

    @Override
    protected boolean resolveGapPeriod() {
        return false;
    }
}
