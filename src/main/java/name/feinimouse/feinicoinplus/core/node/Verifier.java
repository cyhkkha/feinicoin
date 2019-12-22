package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.node.exce.BadCommitException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

// verifier基类
@Component("verifier")
public class Verifier extends CacheNode {

    // 签名串
    protected KeyPair keyPair;
    
    // 节点类型为Verifier
    public Verifier(KeyPair keyPair) {
        super("verifier");
        this.keyPair = keyPair;
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
        return null;
    }

    @Override
    protected void beforeWork() {

    }
}
