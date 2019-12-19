package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.base.HashObj;
import name.feinimouse.feinicoinplus.core.base.SignObj;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignGen {
    String sign(PrivateKey key, String msg);
    SignObj sign(PrivateKey key, SignObj o, String signer);
    boolean verify(PublicKey key, String sign, String msg);
    SignObj genSignObj(PrivateKey key, HashObj h, String signer);
    KeyPair genKeyPair();
}
