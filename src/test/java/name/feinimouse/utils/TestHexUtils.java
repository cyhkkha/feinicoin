package name.feinimouse.utils;

import java.security.SignatureException;
import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import name.feinimouse.simplecoin.feinism2.SM2;
import name.feinimouse.simplecoin.feinism2.SM2Generator;

public class TestHexUtils {
    private String test = "I love Liuchang";
    private String testStr = "154fcfe61aab";
    private byte[] testBytes = new byte[]{ 21, 79, -49, -26, 26, -85 };
    private SM2 sm2;

    @Before
    public void setup() {
        sm2 = SM2Generator.getInstance().generateSM2();
    }

    @Test
    public void testToStr() {
        var res = HexUtils.byteToHex(testBytes);
        var res2 = Hex.toHexString(testBytes);
        System.out.println(res);
        System.out.println(res2);
        Assert.assertEquals(res, testStr);
        Assert.assertEquals(res2, testStr);

    }

    @Test
    public void testToByte() {
        var res = HexUtils.hexToByte(testStr);
        var res2 = Hex.decode(testStr);
        System.out.println(Arrays.toString(res));
        System.out.println(Arrays.toString(res2));
        Assert.assertArrayEquals(res, testBytes);
        Assert.assertArrayEquals(res2, testBytes);

    }

    @Test
    public void testAll() throws SignatureException {
        var sign = sm2.signToByte(test);
        System.out.println(Arrays.toString(sign));

        var signStr = HexUtils.byteToHex(sign);

        var signBak = HexUtils.hexToByte(signStr);
        System.out.println(Arrays.toString(signBak));

        Assert.assertArrayEquals(sign, signBak);
    }
}