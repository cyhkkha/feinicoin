package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.feinicoinplus.core.BaseSignObj;
import name.feinimouse.feinicoinplus.core.SignObj;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class SM2SignGen implements SignGen{
    
    private KeyPairGenerator generator;

    public SM2SignGen() {
        // 国密标准SM2的椭圆曲线的生成参数
        X9ECParameters sm2ECParams = GMNamedCurves.getByName("sm2p256v1");
        // 椭圆曲线实例
        ECNamedCurveParameterSpec sm2EC = new ECNamedCurveParameterSpec(
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
            throw new RuntimeException("SM2椭圆曲线生成失败！");
        }
    }

    // 这里注意Signature对象是不能复用的（我写的时候没有找到复用的办法）
    @Override
    public String sign(PrivateKey key, String msg) {
        try {
            Signature signature = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider()
            );
            signature.initSign(key);
            signature.update(msg.getBytes(StandardCharsets.UTF_8));
            return Hex.toHexString(signature.sign());
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("签名失败");
        }
    }

    @Override
    public <T> SignObj<T> sign(PrivateKey key, SignObj<T> signObj, String signer) {
        String s = sign(key, signObj.gainHash());
        return signObj.putSign(signer, s);
    }

    @Override
    public <T> SignObj<T> sign(PrivateKey key, HashObj<T> hashObj, String signer) {
        String s = sign(key, hashObj.gainHash());
        return new BaseSignObj<>(hashObj).putSign(signer, s);
    }

    // 这里注意Signature对象是不能复用的（我写的时候没有找到复用的办法）
    public boolean verify(PublicKey key, String sign, String msg) {
        try {
            Signature signature = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider()
            );
            signature.initVerify(key);
            signature.update(msg.getBytes(StandardCharsets.UTF_8));
            return signature.verify(sign.getBytes(StandardCharsets.UTF_8));
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            System.out.println("验签失败");
            return false;
        }
    }

    @Override
    public <T> boolean verify(PublicKey key, SignObj<T> signObj, String signer) {
        return verify(key, signObj.getSign(signer), signObj.gainHash());
    }
    

    @Override
    public KeyPair genKeyPair() {
        return generator.genKeyPair();
    }
}
