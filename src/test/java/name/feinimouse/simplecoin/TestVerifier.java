package name.feinimouse.simplecoin;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.utils.LoopUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.SignatureException;

/**
 * Create by 菲尼莫斯 on 2019/6/30
 * Email: cyhkkha@gmail.com
 * File name: TestVerifier
 * Program : feinicoin
 * Description :
 */
public class TestVerifier extends TestTransactionGen {
    protected SimpleVerifier verifier;
    
    @Before
    public void setUp2() {
        verifier = new SimpleVerifier(userManager);
    }
    
    @Test
    public void testVerify() {
        var trans = transGen.genSignedTrans();
        Assert.assertTrue(verifier.verify(trans));
    }
    @Test
    public void testVerifyTime() {
        LoopUtils.loop(100, () -> verifier.verify(transGen.genSignedTrans()));
        collectTime(verifier.getVerifyTimes(), "验签");
    }
    @Test
    public void testBundle() {
        var transList = LoopUtils.loopToList(100, () -> (Transaction)transGen.genSignedTrans());
        Assert.assertTrue(transList.size() > 0);
        var bundle = verifier.bundle(transList);
        System.out.println(bundle.getSummaryJson().toString());
        System.out.println(bundle.getSign().toString("verifier"));
        System.out.printf("打包时间：%f s \n", bundle.getBundleTime() / 1000000000f);
        collectTime(verifier.getVerifyTimes(), "验签");
        System.out.printf("签名和打包时间：%f s \n", verifier.getBundleTimes().get(0) / 1000000000f);
    }
}
