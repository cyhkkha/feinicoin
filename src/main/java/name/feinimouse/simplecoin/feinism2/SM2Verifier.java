package name.feinimouse.simplecoin.feinism2;

import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: SM2Verifier
 * Program : feinicoin
 * Description :
 */
public class SM2Verifier {
    private Signature signature;

    public SM2Verifier(PublicKey key) throws InvalidKeyException {
        try {
            signature = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider()
            );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        signature.initVerify(key);
    }

    public boolean verify(String msg, String sign) throws SignatureException {
        signature.update(msg.getBytes(StandardCharsets.UTF_8));
        return signature.verify(sign.getBytes(StandardCharsets.UTF_8));
    }
    public boolean verify(String msg, byte[] sign) throws SignatureException {
        signature.update(msg.getBytes(StandardCharsets.UTF_8));
        return signature.verify(sign);
    }
}
