package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.NodeBusyException;
import name.feinimouse.feinicoinplus.core.exception.RequestNotSupportException;
import name.feinimouse.lambda.CustomRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

// verifier基类
public abstract class Verifier extends CacheNode {
    Logger logger = LogManager.getLogger(Verifier.class);
    
    @PropNeeded
    protected VerifierCore verifierCore;

    public Verifier(VerifierCore verifierCore) {
        // 默认缓存的初始容量为无限
        super(NODE_VERIFIER);
        this.verifierCore = verifierCore;
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
            .ifPresent(carrier -> resolveVerify(carrier, verifierCore::verifyTransaction));
        // 资产交易
        Optional.ofNullable(cacheWait.poll(AssetTrans.class))
            .ifPresent(carrier -> resolveVerify(carrier, verifierCore::verifyAssetTrans));
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
        verifierCore.sign(privateKey, packer, address);
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
    
}
