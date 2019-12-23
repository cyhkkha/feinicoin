package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.SignObj;
import name.feinimouse.feinicoinplus.core.block.AssetTrans;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.crypt.SignGen;
import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import name.feinimouse.feinicoinplus.core.node.exce.InvalidStartException;
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
        super("verifier", new Class[]{ Transaction.class, AssetTrans.class });
        this.privateKey = privateKey;
    }

    @Override
    protected boolean resolveMassage(JSONObject json) {
        return false;
    }

    @Override
    protected int beforeCache(JSONObject attach) {
        if (!attach.getString("nodeType").equals("order")) {
            return BAD_COMMUNICATOR;
        }
        if (!attach.has("address")) {
            return BAD_COMMUNICATOR;
        }
        return super.beforeCache(attach);
    }

    @Override
    protected boolean resolveWait() {
        if (cacheWait.hasObject(Transaction.class)) {
            SignAttachObj<Transaction> signAttachObj = cacheWait.get(Transaction.class);
            if (signAttachObj != null) {
                String callbackAddress = signAttachObj.getAttach().getString("address");
                SignObj<Transaction> signObj = signAttachObj.getObj();
                String signer = signObj.obj().getSender();
                verifyAndSendBack(signer, signObj, callbackAddress, Transaction.class);
            }
        }
        if (cacheWait.hasObject(AssetTrans.class)) {
            SignAttachObj<AssetTrans> signAttachObj = cacheWait.get(AssetTrans.class);
            if (signAttachObj != null) {
                String callbackAddress = signAttachObj.getAttach().getString("address");
                SignObj<AssetTrans> signObj = signAttachObj.getObj();
                String signer = signObj.obj().getOperator();
                verifyAndSendBack(signer, signObj, callbackAddress, AssetTrans.class);
            }
        }
        return true;
    }
    
    private <T> void verifyAndSendBack(String signer, SignObj<T> signObj, String address, Class<T> tClass) {
        PublicKey key = publicKeyHub.getKey(signer);
        boolean result = signGen.verify(key, signObj, signer);
        signGen.sign(privateKey, signObj, address);
        JSONObject backAttach = nodeMsg().put("type", MSG_VERIFIER_CALLBACK).put("result", result);
        SignAttachObj<T> callback = new SignAttachObj<>(signObj, backAttach);
        network.commit(address, callback, tClass);
    }

    // 空窗期继续运行
    @Override
    protected boolean resolveGapPeriod() {
        return true;
    }

    @Override
    public <T> SignAttachObj<T> fetch(JSONObject json, Class<T> tClass) throws BadCommitException {
        throw new BadCommitException("fetch not support");
    }

    @Override
    public JSONObject fetch(JSONObject json) throws BadCommitException {
        throw new BadCommitException("fetch not support");
    }

    @Override
    public synchronized int commit(JSONObject json) {
        return METHOD_NOT_SUPPORT;
    }

    @Override
    protected void beforeWork() throws InvalidStartException {
        if (signGen == null || publicKeyHub == null) {
            throw new InvalidStartException("verifier has not been set a signGen or a publicKekHub");
        }
    }
}
