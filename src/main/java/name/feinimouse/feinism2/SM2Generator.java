package name.feinimouse.feinism2;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.security.*;

/**
 * Create by 菲尼莫斯 on 2019/6/23 Email: cyhkkha@gmail.com File name: SM2Generator
 * Program : feinicoin Description :
 */
public class SM2Generator {
    // 单例
    private static SM2Generator sm2;
    // 秘钥生成器
    private KeyPairGenerator generator;
    // 国密标准SM2的椭圆曲线的生成参数
    private X9ECParameters sm2ECParams;
    // 椭圆曲线实例
    private ECNamedCurveParameterSpec sm2EC;
    // 是否错误
    private static boolean isError = false;

    private SM2Generator() {
        sm2ECParams = GMNamedCurves.getByName("sm2p256v1");
        sm2EC = new ECNamedCurveParameterSpec(
                // 椭圆曲线的OID
                GMObjectIdentifiers.sm2p256v1.toString(),
                // 方程
                sm2ECParams.getCurve(),
                // 椭圆曲线G点
                sm2ECParams.getG(),
                // 大整数N
                sm2ECParams.getN());
        try {
            generator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            generator.initialize(sm2EC, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
            isError = true;
        }
    }

    public static SM2Generator getInstance() {
        if (sm2 == null) {
            sm2 = new SM2Generator();
        }
        return isError ? null : sm2;
    }

    // 生成密钥对
    public KeyPair generateKeys() {
        return generator.generateKeyPair();
    }

    public SM2 generateSM2() {
        try {
            return new SM2(generateKeys());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
