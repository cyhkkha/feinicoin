package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.exception.*;

import java.lang.reflect.Field;
import java.util.Optional;

// 一个节点即是一个线程
public abstract class BaseNode extends Thread implements Node {

    // 节点类型
    @Getter
    protected String nodeType;

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
    protected long taskInterval = 10;

    // 是否正在运行
    protected boolean runningTag = false;

    // 节点必须有类型
    public BaseNode(String nodeType) {
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
            afterWork();
            return;
        }
        runningTag = true;
        while (runningTag) {
            // 真正的线程运行内容在这里
            try {
                working();
                if (runningTag) {
                    Thread.sleep(taskInterval);
                }
            } catch (NodeStopException | InterruptedException e) {
                System.out.println(e.getMessage());
                stopNode();
            } catch (NodeRunningException e) {
                resolveRunningException(e);
            }
        }
        afterWork();
    }

    protected void resolveRunningException(NodeRunningException e) {
        e.printStackTrace();
        stopNode();
    }

    // 启动前的检查
    protected void runningCheck() throws NodeRunningException {
        Class<?> c = this.getClass();
        internalRunningCheck(c);
        while (!c.getSuperclass().equals(Thread.class)) {
            c = c.getSuperclass();
            internalRunningCheck(c);
        }
    }

    private void internalRunningCheck(Class<?> c) throws NodeRunningException {
        // 检查必要属性@PropNeeded
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(PropNeeded.class) != null) {
                try { // 可以访问私有属性
                    field.setAccessible(true);
                    // 如果未设置属性则抛出异常
                    if (field.get(this) == null) {
                        throw new UninitializedException(this, field.getName());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 在节点运行前的动作
    @SuppressWarnings("RedundantThrows")
    protected void beforeWork() throws NodeRunningException {
    }

    // 节点运行后的动作，一般来说用来释放资源
    protected void afterWork() {
    }

    // 真正的节点的运行工作
    protected abstract void working() throws NodeRunningException;

    // 向节点提交一条消息
    @Override
    public void commit(Carrier carrier) throws BadRequestException {
        requestCheck(carrier);
        beforeCommit(carrier);
        resolveCommit(carrier);
    }

    // 向节点拉取一条信息，节点将返回拉取的结果
    @Override
    public Carrier fetch(Carrier carrier) throws BadRequestException {
        requestCheck(carrier);
        beforeFetch(carrier);
        return resolveFetch(carrier);
    }

    // 请求的统一检查
    protected void requestCheck(Carrier carrier) throws BadRequestException {
        if (isStop()) {
            throw new NodeNotWorkingException(this);
        }
        // 不存在 NetInfo 则为非法交易
        NetInfo netInfo = Optional.ofNullable(carrier)
            .map(Carrier::getNetInfo)
            .orElseThrow(() -> new InvalidRequestException(this, null));
        // 消息必须有发送者
        if (netInfo.getSender() == null) {
            throw new InvalidRequestException(this, netInfo);
        }
        // 接收者需要和本机一样
        if (netInfo.getReceiver() == null || !netInfo.getReceiver().equals(address)) {
            throw new InvalidRequestException(this, netInfo);
        }
        // 回调人未指定，则将其设为发送者
        if (netInfo.getCallback() == null) {
            netInfo.setCallback(netInfo.getSender());
        }
        // 若没有附加信息则创建一个空的附加信息
        if (carrier.getAttachInfo() == null) {
            carrier.setAttachInfo(new AttachInfo());
        }
    }

    protected void beforeCommit(Carrier carrier) throws BadRequestException {
    }

    protected void beforeFetch(Carrier carrier) throws BadRequestException {
    }

    // 处理提交
    protected abstract void resolveCommit(Carrier carrier) throws BadRequestException;

    // 处理拉取
    protected abstract Carrier resolveFetch(Carrier carrier) throws BadRequestException;

    // 停止节点的运行
    @Override
    public void stopNode() {
        if (runningTag) {
            runningTag = false;
        }
    }

    // 获取节点的运行状态
    @Override
    public boolean isStop() {
        return !runningTag;
    }

    protected Carrier genCarrier(String receiver, String msgType, AttachInfo attach) {
        NetInfo netInfo = new NetInfo(nodeType, network.getAddress());
        netInfo.setMsgType(msgType);
        netInfo.setReceiver(receiver);
        netInfo.setSender(address);
        netInfo.setCallback(address);
        AttachInfo nextMsg = Optional.ofNullable(attach).orElseGet(AttachInfo::new).copy();
        return new Carrier(netInfo, nextMsg);
    }

    protected void commitToNetwork(Carrier carrier, Packer packer) {
        carrier.setPacker(packer);
        network.commit(carrier);
    }

    protected Carrier fetchFromNetWork(Carrier fetchCarrier, Class<?> fetchClass) throws BadRequestException {
        fetchCarrier.setFetchClass(fetchClass);
        Carrier carrier = network.fetch(fetchCarrier);
        if (carrier != null) {
            requestCheck(carrier);
        }
        return carrier;
    }
}
