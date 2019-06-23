package name.feinimouse.feinism2;


import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.SignatureException;

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
    
    public SM2Signer(Signature signature) {
        this.signature = signature;
    }
    
    public SM2Signer sign(String msg) throws SignatureException {
        signature.update(msg.getBytes(StandardCharsets.UTF_8));
        result = signature.sign();
        return this;
    }
    public byte[] getByte() {
        return result;
    }

    @Override
    public String toString() {
        return new String(result);
    }
}
