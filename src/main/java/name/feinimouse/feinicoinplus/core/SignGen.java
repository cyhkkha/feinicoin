package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.Packer;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

// 这里注意一个原则，我们只对hash进行签名
public interface SignGen {
    // 签名一段信息
    String sign(PrivateKey key, String msg);
    // 添加一个签名
    Packer sign(PrivateKey key, Packer packer, String signer);
    // 验证一条信息
    boolean verify(PublicKey key, String sign, String msg);
    // 验证某人的签名
    boolean verify(PublicKey key, Packer packer, String signer);
    // 生成一个密钥对
    KeyPair genKeyPair();
}
