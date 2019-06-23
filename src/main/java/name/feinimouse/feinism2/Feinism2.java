package name.feinimouse.feinism2;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.security.*;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: Feinism2
 * Program : feinicoin
 * Description :
 */
public class Feinism2 {
    // 单例
    private static Feinism2 sm2;
    // 秘钥生成器
    private KeyPairGenerator generator;
    // 国密标准SM2的椭圆曲线的生成参数
    private X9ECParameters sm2ECParams;
    // 椭圆曲线实例
    private ECNamedCurveParameterSpec sm2EC;
    
    private Feinism2() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        sm2ECParams = GMNamedCurves.getByName("sm2p256v1");
        sm2EC = new ECNamedCurveParameterSpec(
            // 椭圆曲线的OID
            GMObjectIdentifiers.sm2p256v1.toString(),
            // 方程
            sm2ECParams.getCurve(),
            // 椭圆曲线G点
            sm2ECParams.getG(),
            // 大整数N
            sm2ECParams.getN()
        );
        generator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        generator.initialize(sm2EC, new SecureRandom());
        
    }
    public static Feinism2 getInstance() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        if (sm2 == null) {
            sm2 = new Feinism2();
        }
        return sm2;
    }
    // 生成密钥对
    public KeyPair generateKeys() {
        return generator.generateKeyPair();
    }
    // 获取签名器
    public SM2Signer getSigner(PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        Signature sm3 = Signature.getInstance(
            GMObjectIdentifiers.sm2sign_with_sm3.toString(),
            new BouncyCastleProvider()
        );
        sm3.initSign(key);
        return new SM2Signer(sm3);
    }
    // 获取验签器
    public SM2Verifier getVerifer(PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        Signature sm3 = Signature.getInstance(
            GMObjectIdentifiers.sm2sign_with_sm3.toString(),
            new BouncyCastleProvider()
        );
        sm3.initVerify(key);
        return new SM2Verifier(sm3);
    }
    
}
