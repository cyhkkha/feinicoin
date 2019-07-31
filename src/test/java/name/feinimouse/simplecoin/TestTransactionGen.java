package name.feinimouse.simplecoin;

import name.feinimouse.utils.LoopUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.SignatureException;
import java.util.Arrays;

/**
 * Create by 菲尼莫斯 on 2019/6/30
 * Email: cyhkkha@gmail.com
 * File name: TestTransactionGen
 * Program : feinicoin
 * Description :
 */
public class TestTransactionGen extends SetupTest {
    
    @Test
    public void testUserManager() {
        var randUser = userManager.getRandomUser();
        System.out.println(randUser);
        Assert.assertTrue(ArrayUtils.contains(USERS, randUser));
        LoopUtils.loop(2 * USERS.length, () -> 
            Assert.assertNotEquals(userManager.getRandomUser(randUser), randUser));
        Assert.assertNotNull(userManager.getSM2(randUser));
    }
    
    @Test
    public void testTransGen() throws SignatureException {
        var trans = transGen.genTransaction();
        System.out.println(trans.getSummary());
        Assert.assertNotNull(trans.getSummary());
        var sign = transGen.sign(trans).getSign().getByte("sender");
        System.out.println(Arrays.toString(sign));
        Assert.assertNotNull(sign);
        var sm2 = userManager.getSM2(trans.getSender());
        Assert.assertTrue(sm2.verify(trans.getSummary(), sign));
    }
    @Test
    public void testSignTimes() {
        LoopUtils.loop(10, transGen::genSignedTrans);
        collectTime(transGen.getSignTimes(), "第一次签名");
        transGen.getSignTimes().clear();
        Assert.assertTrue(transGen.getSignTimes().size() < 1);
        LoopUtils.loop(1000, transGen::genSignedTrans);
        collectTime(transGen.getSignTimes(), "签名");
    }
    
}
