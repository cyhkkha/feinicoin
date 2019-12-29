package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import name.feinimouse.feinicoinplus.core.exception.NodeRunningException;
import name.feinimouse.utils.ClassMapContainer;
import name.feinimouse.utils.OverFlowException;
import name.feinimouse.utils.UnrecognizedClassException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Order基类
//@Component("order")
public class Order extends CacheNode {

    // 等待center拉取的交易
    private ClassMapContainer<Carrier> fetchWait;
    // 已经发往verifier等待结果中的交易消息，String为该条消息携带交易的hash
    private Map<String, Carrier> verifyWait;

    // verifier群的地址
    @Getter
    @Setter
    protected String verifiersAddress;

    public Order() {
        // 缓存的默认容量是30
        super(NODE_ORDER, new CarrierAttachCMC(new Class[]{Transaction.class, AssetTrans.class}, 30));
        // 供拉取的缓存默认容量是无限
        fetchWait = new CarrierFetchCMC(new Class[]{Transaction.class, AssetTrans.class});
        verifyWait = new ConcurrentHashMap<>();
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) {
        return fetchWait.poll(carrier.getFetchClass());
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {
        NodeMessage nodeMessage = carrier.getNodeMessage();
        if (nodeMessage.notMatch(NODE_ENTER, MSG_COMMIT_ORDER)
            && nodeMessage.notMatch(NODE_VERIFIER, MSG_CALLBACK_VERIFIER)) {
            throw BadCommitException.commitNotSupportException(this, nodeMessage);
        }
    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {
        if (carrier.notMatchFetch(NODE_CENTER, MSG_FETCH_ORDER, Transaction.class)
            || carrier.notMatchFetch(NODE_CENTER, MSG_FETCH_ORDER, AssetTrans.class)) {
            throw BadCommitException.classNotSupportException(this, carrier.getFetchClass());
        }
    }

    @Override
    protected void afterWork() {
        fetchWait.clear();
        verifyWait.clear();
        super.afterWork();
    }

    @Override
    protected void beforeWork() throws NodeRunningException {
        if (verifiersAddress == null) {
            throw NodeRunningException
                .invalidStartException("Order has not been set a verifiersAddress: " + nodeMsg().toString());
        }
        super.beforeWork();
    }

    @Override
    protected void resolveCache() {
        if (cacheWait.hasObject(Transaction.class)) {
            Carrier carrier = cacheWait.poll(Transaction.class);
            resolveCarrier(carrier);
        }
        if (cacheWait.hasObject(AssetTrans.class)) {
            Carrier carrier = cacheWait.poll(AssetTrans.class);
            resolveCarrier(carrier);
        }
    }
    
    protected void resolveCarrier(Carrier carrier) {
        if (carrier != null) {
            NodeMessage nodeMessage = carrier.getNodeMessage();
            if (nodeMessage.getMsgType() == MSG_COMMIT_ORDER) {
                recordToVerify(carrier);
            }
            if (nodeMessage.getMsgType() == MSG_CALLBACK_VERIFIER) {
                resolveVerifyCallback(carrier);
            }
        }
    }

    protected void recordToVerify(Carrier carrier) {
        Packer packer = carrier.getPacker();
        // 对于重复交易直接忽略
        if (verifyWait.containsKey(packer.gainHash())) {
            return;
        }
        // 存入verifier备案
        verifyWait.put(packer.gainHash(), carrier);
        AttachMessage attachMessage = carrier.getAttachMessage();
        Carrier nextCarrier = genCarrier(verifiersAddress, MSG_COMMIT_VERIFIER, attachMessage);
        // 提交给verifier集群
        commitToNetwork(nextCarrier, packer);
    }

    protected void resolveVerifyCallback(Carrier carrier) {
        Packer packer = carrier.getPacker();
        
        // 查看是否备案
        String hash = packer.gainHash();
        Carrier origin = verifyWait.get(hash);
        // 若没有含verify的备案状态，则直接丢弃
        if (origin == null) {
            return;
        }
        // 删除该备案状态
        verifyWait.remove(hash);
        
        // 查看是否为已验证交易
        AttachMessage attachMessage = carrier.getAttachMessage();
        // 没有标识验证者则尝试从新走验证流程，若缓存溢出则丢弃
        if (attachMessage.getVerifier() == null) {
            try {
                cacheWait.put(origin);
            } catch (UnrecognizedClassException | OverFlowException ex) {
                ex.printStackTrace();
                sendBackError();
            }
            return;
        }
        // 如检测到verifier，但没有验证结果和签名，则证明是非法交易
        if (attachMessage.getVerifiedResult() == null 
            ||packer.getSign(attachMessage.getVerifier()) == null) {
            sendBackError();
            return;
        }
        // 若验证为非法交易则返回原因
        if (!attachMessage.getVerifiedResult()) {
            sendBackError();
            return;
        }
        
        try {
            attachMessage.setOrder(address);
            if (attachMessage.getEnter() == null) {
                attachMessage.setEnter(origin.getAttachMessage().getEnter());
            }
            Carrier nextCarrier = genCarrier(null, MSG_FETCH_ORDER, attachMessage);
            nextCarrier.setPacker(packer);
            fetchWait.put(carrier);
        } catch (UnrecognizedClassException | OverFlowException e) {
            // 理论上该错误不会发生
            e.printStackTrace();
        }
    }
    
    protected void sendBackError() {}
    
    @Override
    protected void resolveGapPeriod() {
    }
}
