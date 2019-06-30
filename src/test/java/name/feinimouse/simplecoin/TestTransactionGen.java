package name.feinimouse.simplecoin;

import name.feinimouse.utils.LoopUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/6/30
 * Email: cyhkkha@gmail.com
 * File name: TestTransactionGen
 * Program : feinicoin
 * Description :
 */
public class TestTransactionGen {
    protected final static String[] USERS = {"kasumi", "arisa", "tae", "rimi", "saaya", "yukina", "sayo", "risa", "rinko", "ako"};
    protected UserManager userManager;
    protected TransactionGen transGen;
    
    @Before
    public void setup() {
        userManager = new UserManager(USERS);
        transGen = new TransactionGen(userManager);
    }
    
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
        LoopUtils.loop(10, () -> transGen.genSignedTrans());
        collectTime(transGen.getSignTimes(), "第一次签名");
        transGen.getSignTimes().clear();
        Assert.assertTrue(transGen.getSignTimes().size() < 1);
        LoopUtils.loop(1000, () -> transGen.genSignedTrans());
        collectTime(transGen.getSignTimes(), "签名");
    }
    
    public void collectTime(List<Long> timeList, String name) {
        var count = timeList.stream().reduce((a, b) -> a + b).orElse(0L);
        System.out.printf("%s总计运行时间: %f s \n", name, (count / 1000000000f));
        System.out.printf("%s平均运行时间: %f s \n", name, count / timeList.size() / 1000000000f);
    }
}
