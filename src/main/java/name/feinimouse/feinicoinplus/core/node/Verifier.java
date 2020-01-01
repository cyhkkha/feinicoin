package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.PropNeeded;
import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.SignGen;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.exception.BadCommitException;
import name.feinimouse.feinicoinplus.core.lambda.InOutRunner;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

// verifier基类
//@Component("verifier")
public class Verifier extends CacheNode {

    // 签名串
    protected PrivateKey privateKey;
    // 公钥仓库
    @Setter
    @Getter
    @PropNeeded
    protected PublicKeyHub publicKeyHub;
    // 签名机
    @Setter
    @Getter
    @PropNeeded
    protected SignGen signGen;

    // 节点类型为Verifier
    public Verifier(PrivateKey privateKey) {
        // 默认缓存的初始容量为30
        super(NODE_VERIFIER, new CarrierAttachCMC(new Class[]{Transaction.class, AssetTrans.class}, 30));
        this.privateKey = privateKey;
    }

    @Override
    protected void beforeCommit(Carrier carrier) throws BadCommitException {
        NodeMessage nodeMessage = carrier.getNodeMessage();
        if (nodeMessage.notMatch(NODE_ORDER, MSG_COMMIT_VERIFIER)) {
            throw BadCommitException.typeNotSupportException(this, nodeMessage);
        }
    }

    @Override
    protected void beforeFetch(Carrier carrier) throws BadCommitException {
        throw new BadCommitException("fetch not support: " + nodeMsg().toString());
    }

    @Override
    protected void resolveCache() {
        Optional.ofNullable(cacheWait.poll(Transaction.class))
            .ifPresent(carrier -> resolveVerify(carrier, this::resolveTransactionVerify));
        Optional.ofNullable(cacheWait.poll(AssetTrans.class))
            .ifPresent(carrier -> resolveVerify(carrier, this::resolveAssetVerify));
    }

    private boolean resolveAssetVerify(Packer packer) {
        AssetTrans assetTrans = (AssetTrans) packer.obj();
        boolean result;
        {
            String signer = assetTrans.getOperator();
            PublicKey key = publicKeyHub.getKey(signer);
            result = signGen.verify(key, packer, signer);
        }
        if (assetTrans.getTransaction() != null) {
            String signer = assetTrans.getTransaction().getSender();
            PublicKey key = publicKeyHub.getKey(signer);
            result = result || signGen.verify(key, packer, signer);
        }
        return result;
    }
    
    private boolean resolveTransactionVerify(Packer packer) {
        Transaction trans = (Transaction) packer.obj();
        String signer = trans.getSender();
        PublicKey key = publicKeyHub.getKey(signer);
        return signGen.verify(key, packer, signer);
    }

    protected void resolveVerify(Carrier carrier, InOutRunner<Packer, Boolean> verify) {
        Packer packer = carrier.getPacker();
        AttachMessage attachMessage = carrier.getAttachMessage();
        NodeMessage nodeMessage = carrier.getNodeMessage();
        String callbackAddress = nodeMessage.getCallback();
        // 验证并签名
        attachMessage.setVerifier(address);
        attachMessage.setVerifiedResult(verify.run(packer));
        signGen.sign(privateKey, packer, address);
        // 回调给Order
        Carrier nextCarrier = genCarrier(callbackAddress, MSG_CALLBACK_VERIFIER, attachMessage);
        commitToNetwork(nextCarrier, packer);
    }


    // 空窗期继续运行
    @Override
    protected void resolveGapPeriod() {
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        throw new BadCommitException("fetch not support: " + nodeMsg().toString());
    }
}
