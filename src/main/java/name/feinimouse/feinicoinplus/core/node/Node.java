package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import org.json.JSONObject;

// 一个节点即是一个线程
public abstract class Node extends Thread {
    
    public static final int COMMIT_SUCCESS = 0;
    public static final int CACHE_OVERFLOW = 1;
    public static final int METHOD_NOT_SUPPORT = 2;
    public static final int CLASS_UNRECOGNIZED = 3;
    
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
    private boolean isRunning = false;

    // 节点必须有类型
    public Node(String nodeType) {
        this.nodeType = nodeType;
    }

    // 线程运行的具体内容
    @Override
    public void run() {
        // 如果节点未设置网络和地址则自动停止
        if (network == null || address == null) {
            return;
        }
        isRunning = true;
        beforeWork();
        while (isRunning) {
            // 真正的线程运行内容在这里
            working();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 节点被中断则自动停止
                isRunning = false;
            }
        }
        afterWork();
    }

    // 向节点提交一个签名内容，签名内容带有附加的cover信息
    public abstract <T> int commit(SignAttachObj<T> attachObj, Class<T> tClass);

    // 向节点提交一条普通信息
    public abstract int commit(JSONObject json);
    
    // 向节点拉取一条信息，节点将返回拉取的结果
    public abstract <T> SignAttachObj<T> fetch(JSONObject json, Class<T> tClass) throws BadCommitException;
    
    // 向节点拉取一条普通信息
    public abstract JSONObject fetch(JSONObject json) throws BadCommitException;
    
    // 在节点运行前的动作
    protected abstract void beforeWork();
    
    // 节点运行后的动作，一般来说用来释放资源
    protected abstract void afterWork();
    
    // 真正的节点的运行工作
    protected abstract void working();
    
    // 给一个签名消息添加一个cover附加信息，该信息为节点的概况
    protected <T> SignAttachObj<T> coverSign(SignObj<T> signObj) {
        JSONObject nodeMsg = new JSONObject().put("address", address)
            .put("network", network.getAddress())
            .put("type", nodeType);
        return new SignAttachObj<>(signObj, nodeMsg);
    }

    // 停止节点的运行
    public synchronized void stopNode() {
        if (isRunning) {
            interrupt();
            isRunning = false;
        }
    }
    
    // 获取节点的运行状态
    public synchronized boolean isRunning() {
        return isRunning;
    }
}
