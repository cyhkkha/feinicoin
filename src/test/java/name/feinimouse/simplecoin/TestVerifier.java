package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.utils.LoopUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Create by 菲尼莫斯 on 2019/6/30
 * Email: cyhkkha@gmail.com
 * File name: TestVerifier
 * Program : feinicoin
 * Description :
 */
public class TestVerifier extends SetupTest {
    
    @Test
    public void testVerify() {
        var trans = transGen.genSignedTrans();
        Assert.assertTrue(verifier.verify(trans));
    }
    @Test
    public void testVerifyTime() {
        LoopUtils.loop(10, () -> verifier.verify(transGen.genSignedTrans()));
        collectTime(verifier.getVerifyTimes(), "第一次验签");
        verifier.getVerifyTimes().clear();
        Assert.assertTrue(verifier.getVerifyTimes().size() < 1);
        LoopUtils.loop(1000, () -> verifier.verify(transGen.genSignedTrans()));
        collectTime(verifier.getVerifyTimes(), "验签");
    }
    @Test
    public void testBundle() {
        var transList = LoopUtils.loopToList(100, () -> (Transaction)transGen.genSignedTrans());
        Assert.assertTrue(transList.size() > 0);
        var bundle = verifier.bundle(transList);
        System.out.println(bundle.getSummary().toString());
        System.out.println(bundle.getSign().toString("verifier"));
        System.out.printf("打包时间：%f s \n", bundle.getBundleTime() / 1000000000f);
        collectTime(verifier.getVerifyTimes(), "验签");
        System.out.printf("签名和打包时间：%f s \n", verifier.getBundleTimes().get(0) / 1000000000f);
    }
    
    @Test
    public void testBundleTime() {
        testVerifyTime();
        LoopUtils.loop(20, () -> {
            var transList = LoopUtils.loopToList(1000, () -> (Transaction)transGen.genSignedTrans());
            var bundle = verifier.bundle(transList);
        });
        collectTime(verifier.getBundleTimes(), "打包");
    }
    
    @Test
    public void testVerifyUTXOTime() {
        var test = transGen.genUTXOBundle(10);
        Assert.assertEquals(10, test.size());
        var timeList = new LinkedList<Long>();
        LoopUtils.loop(100, () -> {
            var utxo = transGen.genUTXOBundle(10);
            while (!utxo.isEmpty()) {
                var trans = utxo.poll();
                verifier.verify(trans, utxo.getOwner());
            }
            var time = verifier.getVerifyTimes().stream()
                .reduce(Long::sum).orElse(0L);
            timeList.add(time);
            verifier.getVerifyTimes().clear();
        });
        collectTime(timeList, "验证utxo");
    }
    
}
