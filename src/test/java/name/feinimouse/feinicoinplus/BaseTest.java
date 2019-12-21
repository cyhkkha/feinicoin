package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.block.Transaction;
import name.feinimouse.feinicoinplus.core.OrdinaryObj;
import name.feinimouse.utils.HexUtils;
import org.junit.Test;

import java.lang.reflect.Field;
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
        OrdinaryObj<Transaction> obj = new OrdinaryObj<>(new Transaction(), "ssss");
        Field[] fields = obj.getClass().getSuperclass().getDeclaredFields();
        for(Field field: fields) {
            System.out.println(field.getGenericType());
        }
    }
}
