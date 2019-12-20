package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.base.BaseObj;
import name.feinimouse.feinicoinplus.core.base.HashObj;
import name.feinimouse.feinicoinplus.core.base.SignObj;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignGen {
    String sign(PrivateKey key, String msg);
    <T extends BaseObj> SignObj<T> sign(PrivateKey key, SignObj<T> signObj, String signer);
    <T extends BaseObj> SignObj<T> sign(PrivateKey key, HashObj<T> hashObj, String signer);
    boolean verify(PublicKey key, String sign, String msg);
    KeyPair genKeyPair();
}
