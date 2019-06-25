package name.feinimouse.feinism2;


import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: SM2Signer
 * Program : feinicoin
 * Description :
 */
public class SM2Signer {
    private Signature signature;
    private byte[] result;
    
    public SM2Signer(PrivateKey key) throws InvalidKeyException {
        try {
            signature = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider()
            );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        signature.initSign(key);
    }
    
    public SM2Signer sign(String msg) throws SignatureException {
        signature.update(msg.getBytes(StandardCharsets.UTF_8));
        result = signature.sign();
        return this;
    }
    public byte[] getByte() {
        return result;
    }
}
