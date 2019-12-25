package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import name.feinimouse.feinicoinplus.core.node.exce.NodeRunningException;
import name.feinimouse.utils.ClassMapContainer;
import name.feinimouse.utils.OverFlowException;
import name.feinimouse.utils.UnrecognizedClassException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Order基类
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
        super(NODE_ORDER, new CarrierSubCMC(new Class[]{Transaction.class, AssetTrans.class}, 30));
        // 供拉取的缓存默认容量是无限
        fetchWait = new CarrierSubCMC(new Class[]{Transaction.class, AssetTrans.class});
        verifyWait = new ConcurrentHashMap<>();
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        if (carrier.notMatch(NODE_CENTER, MSG_FETCH_ORDER, SignObj.class)) {
            throw BadCommitException.classNotSupportException(carrier, this);
        }
        return fetchWait.poll(carrier.getFetchClass());
    }

    @Override
    protected void beforeCache(Carrier carrier) throws BadCommitException {
        if (carrier.notMatch(NODE_ENTER, MSG_COMMIT_ORDER, SignObj.class)
            && carrier.notMatch(NODE_VERIFIER, MSG_CALLBACK_VERIFIER, SignObj.class)) {
            throw BadCommitException.classNotSupportException(carrier, this);
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
            if (carrier != null) {
                if (carrier.getMsgType() == MSG_COMMIT_ORDER) {
                    recordToVerify(carrier);
                }
                if (carrier.getMsgType() == MSG_CALLBACK_VERIFIER) {
                    resolveVerifyCallback(carrier);
                }
            }
        }
        if (cacheWait.hasObject(AssetTrans.class)) {
            Carrier carrier = cacheWait.poll(AssetTrans.class);
            if (carrier != null) {
                if (carrier.getMsgType() == MSG_COMMIT_ORDER) {
                    recordToVerify(carrier);
                }
                if (carrier.getMsgType() == MSG_CALLBACK_VERIFIER) {
                    resolveVerifyCallback(carrier);
                }
            }
        }
    }

    protected void recordToVerify(Carrier carrier) {
        SignObj<?> signObj = (SignObj<?>) carrier.getAttach();
        // 对于重复交易直接忽略
        if (verifyWait.containsKey(signObj.gainHash())) {
            return;
        }
        // 存入verifier备案
        verifyWait.put(signObj.gainHash(), carrier);
        // 提交给verifier集群
        commitToNetwork(verifiersAddress, MSG_COMMIT_VERIFIER,
            null, carrier);
    }

    protected void resolveVerifyCallback(Carrier carrier) {
        SignObj<?> signObj = (SignObj<?>) carrier.getAttach();
        String hash = signObj.gainHash();
        Carrier origin = verifyWait.get(hash);
        // 若没有含verify的备案状态，则直接丢弃
        if (origin == null) {
            return;
        }
        // 删除该备案状态
        verifyWait.remove(hash);
        if (signObj.getSign(carrier.getSender()) == null
            || !carrier.getMsg().has("result")) {
            // 如检测到verifier未进行验证则从新走验证流程
            try {
                cacheWait.put(origin);
            } catch (UnrecognizedClassException | OverFlowException ex) {
                // 如果缓存已满则只好从新提出交易请求
                commitToNetwork(origin.getSender(), MSG_CALLBACK_ORDER,
                    new JSONObject().put("error", "The system is busy, please try again later"), origin);
            }
            return;
        }
        if (!carrier.getMsg().getBoolean("result")) {
            // 若验证为非法交易则返回原因
            commitToNetwork(origin.getSender(), MSG_CALLBACK_ORDER, carrier.getMsg(), origin);
            return;
        }
        try {
            // 所有验证通过进入fetch队列
            fetchWait.put(carrier);
        } catch (UnrecognizedClassException | OverFlowException e) {
            // 理论上该错误不会发生
            e.printStackTrace();
        }
    }
    
    @Override
    protected void resolveGapPeriod() {
    }
}
