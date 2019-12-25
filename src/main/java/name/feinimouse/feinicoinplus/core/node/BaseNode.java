package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.CoverObj;
import name.feinimouse.feinicoinplus.core.Node;
import name.feinimouse.feinicoinplus.core.NodeNetwork;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.feinicoinplus.core.exception.NodeStopException;
import org.json.JSONObject;

// 一个节点即是一个线程
public abstract class BaseNode extends Thread implements Node {
    
//    public static final int COMMIT_SUCCESS = 0;
//    public static final int CACHE_OVERFLOW = 1;
//    public static final int METHOD_NOT_SUPPORT = 2;
//    public static final int CLASS_UNRECOGNIZED = 3;
//    public static final int NODE_NOT_WORKING = 4;
//    public static final int CLASS_INCONSISTENT = 5;
//    public static final int BAD_COMMUNICATOR = 6;
    
    public final static int NODE_ORDER = 200;
    public final static int NODE_VERIFIER = 201;
    public final static int NODE_CENTER = 202;
    public final static int NODE_ENTER = 203;

    public final static int MSG_COMMIT_ORDER = 100;
    public final static int MSG_COMMIT_VERIFIER = 101;
    public final static int MSG_CALLBACK_VERIFIER = 102;
    public final static int MSG_FETCH_ORDER = 103;
    public final static int MSG_CALLBACK_ORDER = 104;
    
    // 节点类型
    @Getter
    protected int nodeType;
    
    // 节点的运行网络，和节点发送消息有关
    @Getter @Setter
    protected NodeNetwork network;

    // 节点在网络中的地址
    @Getter @Setter
    protected String address;
    
    // 线程执行间隔
    @Getter @Setter
    protected long interval = 10;
    
    // 是否正在运行
    private boolean runningTag = false;

    // 节点必须有类型
    public BaseNode(int nodeType) {
        this.nodeType = nodeType;
    }

    // 线程运行的具体内容，节点运行完毕后必须调用reset方法重置节点才能再次运行
    @Override
    public void run() {
        // 如果节点未设置网络和地址则自动停止
        try {
            if (network == null || address == null) {
                throw NodeRunningException
                    .invalidStartException("Network or Address of the Node Type: " + nodeType + " are not been set");
            }
            beforeWork();
        } catch (NodeRunningException e) {
            e.printStackTrace();
            return;
        }
        runningTag = true;
        while (runningTag) {
            // 真正的线程运行内容在这里
            try {
                working();
                Thread.sleep(interval);
            } catch (InterruptedException | NodeStopException | NodeRunningException ex) {
                ex.printStackTrace();
                runningTag = false;
            }
        }
        afterWork();
    }
    
    // 向节点提交一条消息
    @Override
    public void commit(Carrier carrier) throws BadCommitException {
        if (isStop()) {
            throw BadCommitException.notWorkException(this);
        }
        if (carrier.getSender() == null) {
            throw BadCommitException.noSenderException(this);
        }
        resolveCommit(carrier);
    }

    // 处理提交
    protected abstract void resolveCommit(Carrier carrier) throws BadCommitException;
    
    // 向节点拉取一条信息，节点将返回拉取的结果
    @Override
    public Carrier fetch(Carrier carrier) throws BadCommitException {
        if (isStop()) {
            throw BadCommitException.notWorkException(this);
        }
        if (carrier.getSender() == null) {
            throw BadCommitException.noSenderException(this);
        }
        return resolveFetch(carrier);
    }
    
    // 处理拉取
    protected abstract Carrier resolveFetch(Carrier carrier) throws BadCommitException;

    // 在节点运行前的动作
    protected abstract void beforeWork() throws NodeRunningException;
    
    // 节点运行后的动作，一般来说用来释放资源
    protected abstract void afterWork();
    
    // 真正的节点的运行工作
    protected abstract void working() throws NodeRunningException, NodeStopException;

    @Override
    public JSONObject nodeMsg() {
        return new JSONObject().put("nodeAddress", address)
            .put("networkAddress", network.getAddress())
            .put("nodeType", nodeType);
    }

    protected void commitToNetwork(String receiver, int msgType, JSONObject msg, CoverObj<?> attach, Class<?> attachClass, Class<?> subClass) {
        Carrier carrier = new Carrier(address, network.getAddress(), nodeType);
        carrier.setReceiver(receiver);
        carrier.setMsgType(msgType);
        
        carrier.setMsg(msg);
        carrier.setAttach(attach);
        carrier.setAttachClass(attachClass);
        carrier.setSubClass(subClass);
        network.commit(carrier);
    }
    
    protected void commitToNetwork(String receiver, int msgType, JSONObject msg, Carrier carrier) {
        carrier.setReceiver(receiver);
        carrier.setMsgType(msgType);
        carrier.setSender(address);
        carrier.setNetwork(network.getAddress());
        carrier.setNodeType(nodeType);
        carrier.setMsg(msg);
        network.commit(carrier);
    }
    
    // 停止节点的运行
    @Override
    public synchronized void stopNode() {
        if (runningTag) {
            interrupt();
        }
    }
    // 获取节点的运行状态
    @Override
    public boolean isStop() {
        return !runningTag;
    }
}
