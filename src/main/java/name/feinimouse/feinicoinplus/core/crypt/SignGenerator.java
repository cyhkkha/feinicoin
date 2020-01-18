package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.data.HashSignObj;
import name.feinimouse.feinicoinplus.core.data.Packer;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

// 这里注意一个原则，我们只对hash进行签名
public interface SignGenerator {
    // 签名一段信息
    String sign(PrivateKey key, String msg);
    // 添加一个签名
    <T extends HashSignObj> T sign(PrivateKey key, T hashSignObj, String signer);
    // 验证一条信息
    boolean verify(PublicKey key, String sign, String msg);
    // 验证某人的签名
    <T extends HashSignObj> boolean verify(PublicKey key, T hashSignObj, String signer);
    // 生成一个密钥对
    KeyPair genKeyPair();
}
