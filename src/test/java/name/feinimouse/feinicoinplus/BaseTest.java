package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.data.BaseSignObj;
import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.data.BaseHashObj;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class BaseTest {
    @Test
    public void testJava() {
    }
    
    @Test
    public void testGeneric() {
        BaseHashObj<Transaction> obj = new BaseHashObj<>(new Transaction(), "ssss", "hhhhh");
        BaseHashObj<Transaction> obj2 = new BaseHashObj<>(new Transaction(), "ssss", "hhhhh");
        BaseSignObj<Transaction> obj1 = new BaseSignObj<>(obj2);
        ConcurrentHashMap<Class<?>, String> map = new ConcurrentHashMap<>();
        map.put(obj.obj().getClass(), "ssss");
        Assert.assertTrue(map.containsKey(obj1.obj().getClass()));
        System.out.println(map.get(obj1.obj().getClass()));
        Assert.assertEquals(obj.obj().getClass(), Transaction.class);
        
    }
}
