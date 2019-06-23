package name.feinimouse.testcoin;

import name.feinimouse.testcoin.crypt.SM2;
import name.feinimouse.testcoin.crypt.SM2KeyPair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/6/22
 * Email: cyhkkha@gmail.com
 * File name: TestSign
 * Program : feinicoin
 * Description :
 */
public class TestSign {
    
    String[] users = { "小明", "小刚", "小强", "小红" };
    SM2 sm2;
    TransactionGenerator generator;
    
    
    @Before
    public void init() {
        sm2 = new SM2();
        generator = TransactionGenerator.init(users, sm2);
    }
    
    @Test
    public void testGenerateTime() {
        for (var i = 2000; i > 0; i --) {
            generator.generateTransaction();
        }
        var times = generator.getGenerateTimes();
        calculateTime(times, "签名");
    }
    
    void calculateTime(List<Long> times, String name) {
        if (times.size() < 1) {
            System.out.println("No time record");
            return;
        }
        var sum = times.stream().reduce((a, b) -> a + b).get();
        // 平均执行时间
        System.out.println(name + "平均时间");
        System.out.println( sum / times.size());
        // 总执行时间
        System.out.println(name + "总计时间");
        System.out.println( sum );
    }
    
}
