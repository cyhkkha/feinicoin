package name.feinimouse.simplecoin;

import name.feinimouse.feinism2.SM2Generator;
import net.openhft.hashing.LongHashFunction;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.SignatureException;
import java.util.Arrays;

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
    public void testSign() throws SignatureException {
        String msg = "I Love Liuchang";
        var sm2Obj1 = gen.generateSM2();
        var sm2Obj2 = gen.generateSM2();
        var res1 = sm2Obj1.signToByte(msg);
        var res2 = sm2Obj2.signToByte(msg);

        System.out.println("------signer1--------");
        System.out.println(Arrays.toString(res1));
        System.out.println("------signer2--------");
        System.out.println(Arrays.toString(res2));

        Assert.assertNotEquals(res1, res2);
    }

    @Test
    public void testVerify() throws SignatureException {
        String msg = "I Love Liuchang";
        var sm2Obj1 = gen.generateSM2();
        var sm2Obj2 = gen.generateSM2();
        var res1 = sm2Obj1.signToByte(msg);
        var res2 = sm2Obj2.signToByte(msg);
        Assert.assertTrue(sm2Obj1.verify(msg, res1));
        Assert.assertTrue(sm2Obj2.verify(msg, res2));
        Assert.assertFalse(sm2Obj1.verify(msg, res2));
    }
    
    @Test
    public void testHash() {
        var xxHash = LongHashFunction.xx();
        var hash1 = xxHash.hashChars("test");
        var hash2 = xxHash.hashChars("test");
        var hash3 = xxHash.hashChars("tesj");
        var json = new JSONObject().put("test", 111).put("test2","333");
        var hash4 = xxHash.hashChars(json.toString());
        Assert.assertEquals(hash1, hash2);
        Assert.assertNotEquals(hash1, hash3);
        System.out.println(hash1);
        System.out.println(hash4);
    }

}
