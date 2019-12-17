package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.block.HashObj;
import name.feinimouse.feinicoinplus.core.block.SignObj;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignGen {
    String sign(PrivateKey key, String msg);
    boolean verify(PublicKey key, String sign, String msg);
    SignObj genSignObj(PrivateKey key, HashObj hashObj);
    KeyPair genKeyPair();
}
