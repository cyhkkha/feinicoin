package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

// verifier基类
@Component("verifier")
public class Verifier extends CacheNode {

    // 签名串
    protected PrivateKey privateKey;
    
    // 节点类型为Verifier
    public Verifier(PrivateKey privateKey) {
        super("verifier");
        this.privateKey = privateKey;
    }

    @Override
    protected void resolveMassage() {
        
    }

    @Override
    protected void resolveTransaction() {

    }

    @Override
    protected void resolveAssetTrans() {

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
    protected void beforeWork() {

    }
}
