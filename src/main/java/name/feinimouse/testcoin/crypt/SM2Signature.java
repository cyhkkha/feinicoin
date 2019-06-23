package name.feinimouse.testcoin.crypt;

import java.math.BigInteger;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: SM2Signature
 * Program : feinicoin
 * Description :
 */
public class SM2Signature {
    BigInteger r;
    BigInteger s;

    public SM2Signature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    public String toString() {
        return r.toString(16) + "," + s.toString(16);
    }
}
