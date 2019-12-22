package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.BaseHashObj;
import name.feinimouse.feinicoinplus.core.CoverObj;
import name.feinimouse.utils.HexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class BaseTest {
    @Test
    public void testJava() {
        // 100000 - FFFFF
        int test = new Random().nextInt(983040) + 65536;
        System.out.println(Integer.toString(test, 16));
        System.out.println(Integer.toHexString(test));
        System.out.println(HexUtils.randomHexString(16));
        
    }
    
    @Test
    public void testGeneric() {
        BaseHashObj<Transaction> obj = new BaseHashObj<>(new Transaction(), "ssss", "hhhhh");
        Assert.assertEquals(obj.obj().getClass(), Transaction.class);
    }
}
