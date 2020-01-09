package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.node.exception.ControllableException;
import name.feinimouse.feinicoinplus.core.node.exception.RequestNotSupportException;
import name.feinimouse.utils.ClassMapContainer;
import name.feinimouse.utils.exception.OverFlowException;
import name.feinimouse.utils.exception.UnrecognizedClassException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Order基类
//@Component("order")
public abstract class Order extends CacheNode {
    private Logger logger = LogManager.getLogger(Order.class);

    // 等待center拉取的交易
    protected ClassMapContainer<Carrier> fetchWait;
    // 已经发往verifier等待结果中的交易消息，String为该条消息携带交易的hash
    protected Map<String, Carrier> verifyWait;

    // verifier群的地址
    @PropNeeded
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
    protected void beforeFetch(Carrier carrier) throws BadRequestException {
        if (carrier.notMatchFetch(NODE_CENTER, MSG_FETCH_ORDER, Transaction.class)
            || carrier.notMatchFetch(NODE_CENTER, MSG_FETCH_ORDER, AssetTrans.class)) {
            throw new RequestNotSupportException(this, carrier.getNetInfo()
                , "class not support: " + carrier.getFetchClass());
        }
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) {
        Carrier savedCarrier = fetchWait.poll(carrier.getFetchClass());
        if (savedCarrier == null) {
            return null;
        }
        NetInfo netInfo = carrier.getNetInfo();
        Carrier nextCarrier = genCarrier(netInfo.getCallback()
            , MSG_CALLBACK_ORDER, savedCarrier.getAttachInfo());
        nextCarrier.setPacker(savedCarrier.getPacker());
        return nextCarrier;
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadRequestException {
        // NetInfo类型必须支持
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.notMatch(NODE_ENTER, MSG_COMMIT_ORDER)
            && netInfo.notMatch(NODE_VERIFIER, MSG_CALLBACK_VERIFIER)) {
            throw new RequestNotSupportException(this, netInfo, "not support");
        }
        Packer packer = carrier.getPacker();
        // 必须有enter源
        if (packer.getEnter() == null) {
            throw new BadRequestException("Invalid request origin");
        }
        // 必须有发起者的签名
        if (packer.objClass().equals(Transaction.class)
            && packer.excludeSign(((Transaction) packer.obj()).getSender())) {
            throw new BadRequestException("Invalid packer signature");
        } else if (packer.objClass().equals(AssetTrans.class)) {
            AssetTrans assetTrans = (AssetTrans) packer.obj();
            // 正常情况下仅判断操作者是否签了名
            if (packer.excludeSign(assetTrans.getOperator())) {
                throw new BadRequestException("Invalid packer signature");
            }
            // 如果为携带交易的asset，则判断交易者是否也一并签了名
            if (Optional.ofNullable(assetTrans.getTransaction())
                .map(Transaction::getSender)
                .map(packer::excludeSign)
                .orElse(false)) {
                throw new BadRequestException("Invalid packer signature");
            }
        }
    }

    @Override
    protected void afterWork() {
        super.afterWork();
        fetchWait.clear();
        verifyWait.clear();
    }

    @Override
    protected void resolveCache() {
        Optional.ofNullable(cacheWait.poll(Transaction.class)).ifPresent(this::resolveCarrier);
        Optional.ofNullable(cacheWait.poll(AssetTrans.class)).ifPresent(this::resolveCarrier);
    }


    private void resolveCarrier(Carrier carrier) {
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.getMsgType().equals(MSG_COMMIT_ORDER)) {
            recordToVerify(carrier);
            logger.trace("已处理Enter缓存，交由Verifier验证");
        }
        if (netInfo.getMsgType().equals(MSG_CALLBACK_VERIFIER)) {
            try {
                resolveVerifyCallback(carrier);
                logger.trace("已处理验证结果缓存，存入待拉取缓存");
            } catch (ControllableException e) {
                logger.warn(e.getMessage());
                sendBackError();
            }
        }
    }

    private void recordToVerify(Carrier carrier) {
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

    private void resolveVerifyCallback(Carrier carrier) throws ControllableException {
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
        if (packer.getVerifier() == null) {
            try {
                cacheWait.put(origin);
            } catch (UnrecognizedClassException | OverFlowException e) {
                e.printStackTrace();
                throw new ControllableException("re-verify cache overflow");
            }
        }

        // 如检测到verifier，但没有验证结果和签名，则证明是非法交易
        if (packer.getSign(packer.getVerifier()) == null) {
            throw new ControllableException("Invalid verification");
        }

        // 若验证为非法交易则返回原因
        boolean verifyResult = Optional.ofNullable(attachInfo.getVerifiedResult())
            .orElseThrow(() -> new ControllableException("Invalid verification"));
        if (!verifyResult) {
            throw new ControllableException("Verification failed");
        }

        try {
            packer.setOrder(address);
            // 指定好Carrier的fetch类型存入缓存
            carrier.setFetchClass(packer.objClass());
            fetchWait.put(carrier);
        } catch (UnrecognizedClassException | OverFlowException e) {
            // 理论上该错误不会发生
            logger.error(e.getMessage());
        }
    }

    // 将交易错误的信息返回enter
    protected void sendBackError() {
    }
}
