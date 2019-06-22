package name.feinimouse.crypt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Create by 菲尼莫斯 on 2019/6/22
 * Email: cyhkkha@gmail.com
 * File name: TestCrypt
 * Program : feinicoin
 * Description :
 */
public class TestCrypt {
    
    private String[] users = { "小明", "小刚", "小强", "小红" };
    private SM2 sm2;
    private SM2KeyPair testKey;
    private Generator generator;
    
    
    
    @Before
    public void testGen() {
        sm2 = new SM2();
        testKey = sm2.generateKeyPair();
        generator = Generator.getGenerator(users, sm2);
    }
    
    @Test
    public void testGenerateTime() {
        for (var i = 2000; i > 0; i --) {
            generator.generateTransaction();
        }
        var times = generator.getGenerateTimes();
        var sum = times.stream().reduce((a, b) -> a + b).get();
        // 平均执行时间
        System.out.println("平均时间");
        System.out.println( sum / times.size());
        // 总执行时间
        System.out.println("总计时间");
        System.out.println( sum );
    }
    
    
}
