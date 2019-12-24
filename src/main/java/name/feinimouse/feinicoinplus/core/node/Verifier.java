package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.crypt.SignGen;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import name.feinimouse.feinicoinplus.core.node.exce.NodeRunningException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

// verifier基类
@Component("verifier")
public class Verifier extends CacheNode {

    // 签名串
    protected PrivateKey privateKey;
    // 公钥仓库
    @Setter @Getter
    protected PublicKeyHub publicKeyHub;
    // 签名机
    @Setter @Getter
    protected SignGen signGen;
    
    // 节点类型为Verifier
    public Verifier(PrivateKey privateKey) {
        super(NODE_VERIFIER, new CarrierSubCMC(new Class[]{ Transaction.class, AssetTrans.class }));
        this.privateKey = privateKey;
//        supportAttachClass = new Class[] { SignObj.class };
//        supportCommitType = new int[] { MSG_COMMIT_VERIFIER };
    }

    @Override
    protected void beforeCache(Carrier carrier) throws BadCommitException {
        if (carrier.notMatch(NODE_ORDER, MSG_COMMIT_VERIFIER, SignObj.class)) {
            throw BadCommitException.methodNotSupportException(carrier, this);
        }
    }

    @Override
    protected void resolveCache() {
        if (cacheWait.hasObject(Transaction.class)) {
            Carrier carrier = cacheWait.get(Transaction.class);
            if (carrier != null) {
                @SuppressWarnings("unchecked")
                SignObj<Transaction> signObj = (SignObj<Transaction>) carrier.getAttach();
                String callbackAddress = carrier.getSender();
                String signer = signObj.obj().getSender();
                JSONObject result = getVerifyResultAndSign(signer, signObj);
                commitToNetwork(callbackAddress, MSG_VERIFIER_CALLBACK, result, signObj
                    , SignObj.class, Transaction.class);
            }
        }
        if (cacheWait.hasObject(AssetTrans.class)) {
            Carrier carrier  = cacheWait.get(AssetTrans.class);
            if (carrier != null) {
                @SuppressWarnings("unchecked")
                SignObj<AssetTrans> signObj = (SignObj<AssetTrans>) carrier.getAttach();
                String callbackAddress = carrier.getSender();
                String signer = signObj.obj().getOperator();
                JSONObject result = getVerifyResultAndSign(signer, signObj);
                commitToNetwork(callbackAddress, MSG_VERIFIER_CALLBACK, result, signObj
                    , SignObj.class, AssetTrans.class);
            }
        }
    }
    
    private JSONObject getVerifyResultAndSign(String signer, SignObj<?> signObj) {
        PublicKey key = publicKeyHub.getKey(signer);
        JSONObject result = new JSONObject().put("result", signGen.verify(key, signObj, signer));
        signGen.sign(privateKey, signObj, address);
        return result;
    }
    

    // 空窗期继续运行
    @Override
    protected void resolveGapPeriod() {
    }

    @Override
    protected Carrier resolveFetch(Carrier carrier) throws BadCommitException {
        throw new BadCommitException("fetch not support: " + nodeMsg().toString());
    }
    

    @Override
    protected void beforeWork() throws NodeRunningException {
        if (signGen == null || publicKeyHub == null) {
            throw NodeRunningException
                .invalidStartException("verifier has not been set a signGen or a publicKekHub: " + nodeMsg().toString());
        }
    }
}
