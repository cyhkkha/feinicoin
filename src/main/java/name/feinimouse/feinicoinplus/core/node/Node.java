package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import name.feinimouse.feinicoinplus.core.node.exce.InvalidStartException;
import name.feinimouse.feinicoinplus.core.node.exce.NodeRunningException;
import org.json.JSONObject;

// 一个节点即是一个线程
public abstract class Node extends Thread {
    
    public static final int COMMIT_SUCCESS = 0;
    public static final int CACHE_OVERFLOW = 1;
    public static final int METHOD_NOT_SUPPORT = 2;
    public static final int CLASS_UNRECOGNIZED = 3;
    public static final int NODE_NOT_WORKING = 4;
    public static final int CLASS_INCONSISTENT = 5;
    public static final int BAD_COMMUNICATOR = 6;
    
    public static final int MSG_VERIFIER_CALLBACK = 101;
    
    // 节点类型
    @Getter
    protected String nodeType;
    
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
    public Node(String nodeType) {
        this.nodeType = nodeType;
    }

    // 线程运行的具体内容，节点运行完毕后必须调用reset方法重置节点才能再次运行
    @Override
    public void run() {
        // 如果节点未设置网络和地址则自动停止
        try {
            if (network == null || address == null) {
                throw new InvalidStartException("network or address of Node: " + nodeType + " are not been set");
            }
            beforeWork();
        } catch (InvalidStartException e) {
            e.printStackTrace();
            return;
        }
        runningTag = true;
        while (runningTag) {
            // 真正的线程运行内容在这里
            try {
                runningTag = working();
                // 注意调用interrupted方法后中断状态又会置为false
                if (!runningTag || interrupted()) {
                    runningTag = false;
                } else {
                    Thread.sleep(interval);
                }
            } catch (InterruptedException | NodeRunningException ex) {
                ex.printStackTrace();
                runningTag = false;
            }
        }
        afterWork();
    }

    // 向节点提交一个签名内容，签名内容带有附加的节点消息
    public abstract <T> int commit(SignAttachObj<T> attachObj, Class<T> tClass);

    // 向节点提交一条普通信息
    public abstract int commit(JSONObject json);
    
    // 向节点拉取一条信息，节点将返回拉取的结果
    public abstract <T> SignAttachObj<T> fetch(JSONObject json, Class<T> tClass) throws BadCommitException;
    
    // 向节点拉取一条普通信息
    public abstract JSONObject fetch(JSONObject json) throws BadCommitException;
    
    // 在节点运行前的动作
    protected abstract void beforeWork() throws InvalidStartException;
    
    // 节点运行后的动作，一般来说用来释放资源
    protected abstract void afterWork();
    
    // 真正的节点的运行工作，返回结果将作为判断是否继续运行的标准
    protected abstract boolean working() throws NodeRunningException;
    
    // 给一个签名消息添加一个cover附加信息，该信息为节点的概况
    public <T> SignAttachObj<T> coverSign(SignObj<T> signObj) {
        return new SignAttachObj<>(signObj, nodeMsg());
    }

    public JSONObject nodeMsg() {
        return new JSONObject().put("nodeAddress", address)
            .put("networkAddress", network.getAddress())
            .put("nodeType", nodeType);
    }
    
    // 停止节点的运行
    public synchronized void stopNode() {
        if (runningTag) {
            interrupt();
        }
    }
    // 获取节点的运行状态
    public boolean isStop() {
        return !runningTag;
    }
}
