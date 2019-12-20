package name.feinimouse.simplecoin.feinism2;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import org.bouncycastle.util.encoders.Hex;

import lombok.Getter;

/**
 * Create by 菲尼莫斯 on 2019/6/25 
 * Email: cyhkkha@gmail.com 
 * File name: SM2
 * Program : feinicoin 
 * Description :
 */

public class SM2 {
    private SM2Signer signer;
    private SM2Verifier verifier;

    @Getter
    private PrivateKey privateKey;
    @Getter
    private PublicKey publicKey;

    public SM2(KeyPair keyPair) throws InvalidKeyException {
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        signer = new SM2Signer(privateKey);
        verifier = new SM2Verifier(publicKey);
    }

    public byte[] signToByte(String msg) throws SignatureException {
         return signer.sign(msg).getByte();
    }

    public String signToString(String msg) throws SignatureException {
        var bytes = signer.sign(msg).getByte();
        return Hex.toHexString(bytes);
    }

    public boolean verify(String msg, String sign) throws SignatureException {
        var bytes = Hex.decode(sign);
        return verifier.verify(msg, bytes);
    }

    public boolean verify(String msg, byte[] sign) throws SignatureException {
        return verifier.verify(msg, sign);
    }
 }