package name.feinimouse.simplecoin;

import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.feinism2.SM2Signer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.SignatureException;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: TestFeinism2
 * Program : feinicoin
 * Description :
 */
public class TestFeinism2 {
    
    private SM2Generator gen;
    
    @Before
    public void setUp() {
        gen = SM2Generator.getInstance();
        Assert.assertNotNull(gen);
    }
    
    @Test
    public void testGenerator() {
        var key1 = gen.generateKeys();
        var key2 = gen.generateKeys();
        System.out.println(key1.getPrivate());
        System.out.println(key2.getPrivate());
        Assert.assertNotEquals(key1, key2);
    }
    
    @Test
    public void testSign() throws InvalidKeyException, SignatureException {
        String msg = "I Love Liuchang";
        var key = gen.generateKeys();
        var key2 = gen.generateKeys();
        var signer = new SM2Signer(key.getPrivate());
        var signer2 = new SM2Signer(key2.getPrivate());
        var res1 = signer.sign(msg).toString();
        var res2 = signer2.sign(msg).toString();
        System.out.println(res1);
        System.out.println(res2);
        Assert.assertNotEquals(res1, res2);
    }
}
