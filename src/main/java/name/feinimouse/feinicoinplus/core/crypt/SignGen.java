package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.feinicoinplus.core.SignObj;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

// 这里注意一个原则，我们只对hash进行签名
public interface SignGen {
    // 签名一段信息
    String sign(PrivateKey key, String msg);
    // 添加一个签名
    <T> SignObj<T> sign(PrivateKey key, SignObj<T> signObj, String signer);
    // 生成一个签名对象
    <T> SignObj<T> sign(PrivateKey key, HashObj<T> hashObj, String signer);
    // 验证一条信息
    boolean verify(PublicKey key, String sign, String msg);
    // 验证某人的签名
    <T> boolean verify(PublicKey key, SignObj<T> sign, String signer);
    // 生成一个密钥对
    KeyPair genKeyPair();
}
