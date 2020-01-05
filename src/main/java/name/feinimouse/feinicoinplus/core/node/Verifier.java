package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
import name.feinimouse.lambda.InOutRunner;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

// verifier基类
public abstract class Verifier extends CacheNode {
    @PropNeeded
    protected PublicKeyHub publicKeyHub;
    @PropNeeded
    protected SignGenerator signGen;

    // 签名串
    @Setter
    protected PrivateKey privateKey;

    public Verifier(PublicKeyHub publicKeyHub, SignGenerator signGen) {
        // 默认缓存的初始容量为30
        super(NODE_VERIFIER, new CarrierAttachCMC(new Class[]{Transaction.class, AssetTrans.class}, 30));
        this.publicKeyHub = publicKeyHub;
        this.signGen = signGen;
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.notMatch(NODE_ORDER, MSG_COMMIT_VERIFIER)) {
            throw BadCommitException.typeNotSupportException(this, netInfo);
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
        return result;
    }
    
    // 验证普通交易
    private boolean resolveTransactionVerify(Packer packer) {
        Transaction trans = (Transaction) packer.obj();
        String signer = trans.getSender();
        PublicKey key = publicKeyHub.getKey(signer);
        return signGen.verify(key, packer, signer);
    }

    // 总体过程
    protected void resolveVerify(Carrier carrier, InOutRunner<Packer, Boolean> verify) {
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
        commitToNetwork(nextCarrier, packer);
    }
}
