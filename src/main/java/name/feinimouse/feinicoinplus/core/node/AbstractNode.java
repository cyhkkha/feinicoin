package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.util.Optional;

// 一个节点即是一个线程
public abstract class AbstractNode extends Thread implements Node {
    private Logger logger = LogManager.getLogger(AbstractNode.class);
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
    @PropNeeded
    protected String address;

    @Setter
    @PropNeeded
    protected PrivateKey privateKey;

    // 线程执行间隔
    @Getter
    @Setter
    protected long taskInterval = 5;

    // 是否正在运行
    protected boolean runningTag = false;

    // 节点必须有类型
    public AbstractNode(String nodeType) {
        this.nodeType = nodeType;
    }

    public void setAddress(String address) {
        this.address = address;
        setName(nodeType + "@" + address);
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
        logger.trace("即将开始运行");
        while (runningTag) {
            // 真正的线程运行内容在这里
            try {
                working();
                if (runningTag) {
                    if (taskInterval > 0) {
                        Thread.sleep(taskInterval);
                    } else {
                        yield();
                    }
                }
            } catch (NodeStopException | InterruptedException e) {
                logger.info(e.getMessage());
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
    protected void beforeWork() throws NodeRunningException {
    }

    // 节点运行后的动作，一般来说用来释放资源
    protected void afterWork() {
        logger.trace("即将停止运行");
    }

    // 真正的节点的运行工作
    protected abstract void working() throws NodeRunningException;

    // 向节点提交一条消息
    @Override
    public void commit(Carrier carrier) throws BadRequestException {
        requestCheck(carrier);
        beforeCommit(carrier);
        resolveCommit(carrier);
        logger.trace("已收到并处理消息({})", carrier.getNetInfo());
    }

    // 向节点拉取一条信息，节点将返回拉取的结果
    @Override
    public Carrier fetch(Carrier carrier) throws BadRequestException {
        requestCheck(carrier);
        beforeFetch(carrier);
        Carrier ret = resolveFetch(carrier);
        if (ret != null) {
            logger.trace("消息({})拉取了数据({})"
                , carrier.getNetInfo(), ret.getNetInfo());
        }
        return ret;
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
    
    protected void commitToNetwork(Carrier carrier) throws NodeBusyException {
        network.commit(carrier);
    }
    
    protected void commitToNetwork(Carrier carrier, Packer packer) throws NodeBusyException {
        carrier.setPacker(packer);
        commitToNetwork(carrier);
    }

    protected Carrier fetchFromNetWork(Carrier fetchCarrier) throws BadRequestException {
        Carrier carrier = network.fetch(fetchCarrier);
        if (carrier != null) {
            requestCheck(carrier);
        }
        return carrier;
    }
    
    protected Carrier fetchFromNetWork(Carrier fetchCarrier, Class<?> fetchClass) throws BadRequestException {
        fetchCarrier.setFetchClass(fetchClass);
        return fetchFromNetWork(fetchCarrier);
    }
}
