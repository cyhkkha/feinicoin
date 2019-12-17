package name.feinimouse.feinicoinplus.core.crypt;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignGen {
    String sign(PrivateKey key, String msg);
    boolean verify(PublicKey key, String sign, String msg);
    SignObj genSignObj(PrivateKey key, HashObj hashObj);
    KeyPair genKeyPair();
}
