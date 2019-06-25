package name.feinimouse.feinism2;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

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
        var buf = new StringBuilder();
        for(byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                buf.append("0");
            }
            buf.append(hex);
        }
        return buf.toString();
    }

    public boolean verify(String msg, String sign) throws SignatureException {
        var bytes = new byte[sign.length() / 2];
        for (int i = 0; i < sign.length() / 2; i++) {
            var sub = sign.substring(2 * i, 2 * i + 2);
            bytes[i] = (byte)Integer.parseInt(sub, 16);
        }
        return verifier.verify(msg, bytes);
    }

    public boolean verify(String msg, byte[] sign) throws SignatureException {
        return verifier.verify(msg, sign);
    }
 }