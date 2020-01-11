package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.exception.*;
import name.feinimouse.lambda.CustomRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

// verifier基类
public abstract class Verifier extends CacheNode {
    Logger logger = LogManager.getLogger(Verifier.class);
    
    @Setter
    @PropNeeded
    protected PublicKeyHub publicKeyHub;
    @Setter
    @PropNeeded
    protected SignGenerator signGen;

    // 签名串
    @Setter
    protected PrivateKey privateKey;

    public Verifier() {
        // 默认缓存的初始容量为无限
        super(NODE_VERIFIER);
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadRequestException {
        super.beforeCommit(carrier);
        // NetInfo类型必须支持
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.notMatch(NODE_ORDER, MSG_COMMIT_VERIFIER)) {
            throw new RequestNotSupportException(this, netInfo, "not support");
        }
    }

    @Override
    protected void resolveCache() {
        // 普通交易
        Optional.ofNullable(cacheWait.poll(Transaction.class))
            .ifPresent(carrier -> resolveVerify(carrier, this::resolveTransactionVerify));
        // 资产交易
        Optional.ofNullable(cacheWait.poll(AssetTrans.class))
            .ifPresent(carrier -> resolveVerify(carrier, this::resolveAssetVerify));
    }

    // 总体过程
    private void resolveVerify(Carrier carrier, CustomRunner<Packer, Boolean> verify) {
        Packer packer = carrier.getPacker();
        AttachInfo attachInfo = carrier.getAttachInfo();
        NetInfo netInfo = carrier.getNetInfo();
        String callbackAddress = netInfo.getCallback();
        // 验证并签名
        packer.setVerifier(address);
        attachInfo.setVerifiedResult(verify.run(packer));
        signGen.sign(privateKey, packer, address);
        // 回调给Order
        Carrier nextCarrier = genCarrier(callbackAddress, MSG_CALLBACK_VERIFIER, attachInfo);
        try {
            commitToNetwork(nextCarrier, packer);
        } catch (NodeBusyException e) {
            nextCarrier.setPacker(packer);
            resolveOrderBusy(nextCarrier);
        }

    }
    
    protected void resolveOrderBusy(Carrier carrier) {
        logger.warn("order busy !!");
    }

    // 验证资产交易
    private boolean resolveAssetVerify(Packer packer) {
        AssetTrans assetTrans = (AssetTrans) packer.obj();
        boolean result;
        // 先验证资产签名
        {
            String signer = assetTrans.getOperator();
            PublicKey key = publicKeyHub.getKey(signer);
            result = signGen.verify(key, packer, signer);
        }
        // 若有附加交易则验证附加交易
        if (assetTrans.getTransaction() != null) {
            String signer = assetTrans.getTransaction().getSender();
            PublicKey key = publicKeyHub.getKey(signer);
            result = result || signGen.verify(key, packer, signer);
        }
        logger.trace("已验证资产交易");
        return result;
    }
    
    // 验证普通交易
    private boolean resolveTransactionVerify(Packer packer) {
        Transaction trans = (Transaction) packer.obj();
        String signer = trans.getSender();
        PublicKey key = publicKeyHub.getKey(signer);
        boolean result = signGen.verify(key, packer, signer);
        logger.trace("已验证普通交易");
        return result;
    }
    
}
