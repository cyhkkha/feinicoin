package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.PropNeeded;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import name.feinimouse.feinicoinplus.core.exception.ControllableException;
import name.feinimouse.utils.ClassMapContainer;
import name.feinimouse.utils.OverFlowException;
import name.feinimouse.utils.UnrecognizedClassException;

import java.util.Map;
import java.util.Optional;
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
    @PropNeeded
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
        Carrier savedCarrier = fetchWait.poll(carrier.getFetchClass());
        if (savedCarrier == null) {
            return null;
        }
        NetInfo netInfo = carrier.getNetInfo();
        Carrier nextCarrier = genCarrier(netInfo.getCallback(), MSG_CALLBACK_ORDER, savedCarrier.getAttachInfo());
        nextCarrier.setPacker(savedCarrier.getPacker());
        return nextCarrier;
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.notMatch(NODE_ENTER, MSG_COMMIT_ORDER)
            && netInfo.notMatch(NODE_VERIFIER, MSG_CALLBACK_VERIFIER)) {
            throw BadCommitException.typeNotSupportException(this, netInfo);
        }
        Packer packer = carrier.getPacker();
        if (packer.objClass().equals(Transaction.class)
            && packer.excludeSign(((Transaction) packer.obj()).getSender())) {
            throw new BadCommitException("Invalid packer signature");
        } else if (packer.objClass().equals(AssetTrans.class)) {
            AssetTrans assetTrans = (AssetTrans) packer.obj();
            // 正常情况下仅判断操作者是否签了名
            if (packer.excludeSign(assetTrans.getOperator())
                // 如果为携带交易的asset，则判断交易者是否也一并签了名
                || Optional.ofNullable(assetTrans.getTransaction())
                .map(Transaction::getSender)
                .map(packer::excludeSign)
                .orElse(false)) {
                throw new BadCommitException("Invalid packer signature");
            }
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
    protected void resolveCache() {
        Optional.ofNullable(cacheWait.poll(Transaction.class)).ifPresent(this::resolveCarrier);
        Optional.ofNullable(cacheWait.poll(AssetTrans.class)).ifPresent(this::resolveCarrier);
    }

    protected void resolveCarrier(Carrier carrier) {
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.getMsgType() == MSG_COMMIT_ORDER) {
            recordToVerify(carrier);
        }
        if (netInfo.getMsgType() == MSG_CALLBACK_VERIFIER) {
            try {
                resolveVerifyCallback(carrier);
            } catch (ControllableException e) {
                e.printStackTrace();
                sendBackError();
            }
        }
    }

    protected void recordToVerify(Carrier carrier) {
        Packer packer = carrier.getPacker();
        // 对于重复交易直接忽略
        if (verifyWait.containsKey(packer.getHash())) {
            return;
        }
        // 存入verifier备案
        verifyWait.put(packer.getHash(), carrier);
        AttachInfo attachInfo = carrier.getAttachInfo();
        Carrier nextCarrier = genCarrier(verifiersAddress, MSG_COMMIT_VERIFIER, attachInfo);
        // 提交给verifier集群
        commitToNetwork(nextCarrier, packer);
    }

    protected void resolveVerifyCallback(Carrier carrier) throws ControllableException {
        Packer packer = carrier.getPacker();

        // 查看是否备案
        String hash = packer.getHash();
        // 检查是否含verify的备案状态
        Carrier origin = Optional.ofNullable(verifyWait.get(hash))
            .orElseThrow(() -> new ControllableException("Not filed"));

        // 删除该备案状态
        verifyWait.remove(hash);

        // 查看是否为已验证交易
        AttachInfo attachInfo = carrier.getAttachInfo();
        // 没有标识验证者则尝试从新走验证流程，若缓存溢出则丢弃
        if (attachInfo.getVerifier() == null) {
            try {
                cacheWait.put(origin);
            } catch (UnrecognizedClassException | OverFlowException e) {
                e.printStackTrace();
                throw new ControllableException("re-verify cache overflow");
            }
        }

        // 如检测到verifier，但没有验证结果和签名，则证明是非法交易
        if (packer.getSign(attachInfo.getVerifier()) == null) {
            throw new ControllableException("Invalid verification");
        }

        // 若验证为非法交易则返回原因
        boolean verifyResult = Optional.ofNullable(attachInfo.getVerifiedResult())
            .orElseThrow(() -> new ControllableException("Invalid verification"));
        if (!verifyResult) {
            throw new ControllableException("Verification failed");
        }

        try {
            attachInfo.setOrder(address);
            // 若没有检测到Enter则先引用原始交易的Enter
            if (attachInfo.getEnter() == null) {
                attachInfo.setEnter(origin.getAttachInfo().getEnter());
            }
            fetchWait.put(carrier);
        } catch (UnrecognizedClassException | OverFlowException e) {
            // 理论上该错误不会发生
            e.printStackTrace();
        }
    }

    protected void sendBackError() {
    }

    @Override
    protected void resolveGapPeriod() {
    }
}
