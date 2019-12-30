package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.Node;
import name.feinimouse.feinicoinplus.core.NodeNetwork;
import name.feinimouse.feinicoinplus.core.PropNeeded;
import name.feinimouse.feinicoinplus.core.data.AttachMessage;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.NodeMessage;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.feinicoinplus.core.exception.NodeStopException;
import org.json.JSONObject;

import java.lang.reflect.Field;

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
    @Getter
    @Setter
    @PropNeeded
    protected NodeNetwork network;

    // 节点在网络中的地址
    @Getter
    @Setter
    @PropNeeded
    protected String address;

    // 线程执行间隔
    @Getter
    @Setter
    protected long interval = 10;

    // 是否正在运行
    protected boolean runningTag = false;

    // 节点必须有类型
    public BaseNode(int nodeType) {
        this.nodeType = nodeType;
    }

    // 线程运行的具体内容，节点运行完毕后必须调用reset方法重置节点才能再次运行
    @Override
    public void run() {
        // 如果节点未设置网络和地址则自动停止
        try {
            runningCheck();
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
                if (runningTag) {
                    Thread.sleep(interval);
                }
            } catch (InterruptedException | NodeStopException | NodeRunningException ex) {
                ex.printStackTrace();
            }
        }
        afterWork();
    }

    // 启动前的检查
    protected void runningCheck() throws NodeRunningException {
        // 检查必要属性@PropNeeded
        Class<?> c = this.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(PropNeeded.class) != null) {
                try { // 可以访问私有属性
                    field.setAccessible(true);
                    // 如果未设置属性则抛出异常
                    if (field.get(this) == null) {
                        throw NodeRunningException.uninitializedException(this,
                            field.getName() + ":" + field.getType());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 在节点运行前的动作
    protected abstract void beforeWork() throws NodeRunningException;

    // 节点运行后的动作，一般来说用来释放资源
    protected abstract void afterWork();

    // 真正的节点的运行工作
    protected abstract void working() throws NodeRunningException, NodeStopException;

    // 向节点提交一条消息
    @Override
    public void commit(Carrier carrier) throws BadCommitException {
        requestCheck(carrier);
        beforeCommit(carrier);
        resolveCommit(carrier);
    }

    // 向节点拉取一条信息，节点将返回拉取的结果
    @Override
    public Carrier fetch(Carrier carrier) throws BadCommitException {
        requestCheck(carrier);
        beforeFetch(carrier);
        return resolveFetch(carrier);
    }

    // 请求的统一检查
    protected void requestCheck(Carrier carrier) throws BadCommitException {
        if (isStop()) {
            throw BadCommitException.notWorkException(this);
        }
        NodeMessage message = carrier.getNodeMessage();
        if (message == null || message.getSender() == null) {
            throw BadCommitException.noSenderException(this);
        }
        if (message.getCallback() == null) {
            message.setCallback(message.getSender());
        }
        if (carrier.getAttachMessage() == null) {
            carrier.setAttachMessage(new AttachMessage());
        }
    }

    protected abstract void beforeCommit(Carrier carrier) throws BadCommitException;

    protected abstract void beforeFetch(Carrier carrier) throws BadCommitException;

    // 处理提交
    protected abstract void resolveCommit(Carrier carrier) throws BadCommitException;

    // 处理拉取
    protected abstract Carrier resolveFetch(Carrier carrier) throws BadCommitException;

    // 停止节点的运行
    @Override
    public synchronized void stopNode() {
        if (runningTag) {
            runningTag = false;
            interrupt();
        }
    }

    // 获取节点的运行状态
    @Override
    public boolean isStop() {
        return !runningTag;
    }

    @Override
    public JSONObject nodeMsg() {
        return new JSONObject().put("nodeAddress", address)
            .put("networkAddress", network.getAddress())
            .put("nodeType", nodeType);
    }

    protected Carrier genCarrier(String receiver, int msgType, AttachMessage msg) {
        NodeMessage message = new NodeMessage(nodeType, network.getAddress());
        message.setMsgType(msgType);
        message.setReceiver(receiver);
        message.setSender(address);
        message.setCallback(address);
        if (msg == null) {
            msg = new AttachMessage();
        }
        return new Carrier(message, msg);
    }

    protected void commitToNetwork(Carrier carrier, Packer packer) {
        carrier.setPacker(packer);
        network.commit(carrier);
    }

    protected Carrier fetchFromNetWork(Carrier carrier, Class<?> fetchClass) {
        carrier.setFetchClass(fetchClass);
        return network.fetch(carrier);
    }
}
