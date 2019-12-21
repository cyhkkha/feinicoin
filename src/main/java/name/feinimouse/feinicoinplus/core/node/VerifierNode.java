package name.feinimouse.feinicoinplus.core.node;

import java.security.KeyPair;

// verifier基类
public abstract class VerifierNode extends CacheNode {

    // 签名串
    protected KeyPair keyPair;
    
    // 节点类型为Verifier
    public VerifierNode(KeyPair keyPair) {
        super("verifier");
        this.keyPair = keyPair;
    }
    
}
