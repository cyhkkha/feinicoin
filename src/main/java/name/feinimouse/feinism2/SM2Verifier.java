package name.feinimouse.feinism2;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: SM2Verifier
 * Program : feinicoin
 * Description :
 */
public class SM2Verifier {
    private Signature signature;
    private byte[] result;

    public SM2Verifier(Signature signature) {
        this.signature = signature;
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
