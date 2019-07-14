package name.feinimouse.simplecoin;

import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.simplecoin.manager.SimpleVerifier;
import org.junit.Before;

import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/7/4
 * Email: cyhkkha@gmail.com
 * File name: SetupTest
 * Program : feinicoin
 * Description :
 */
public class SetupTest {
    final static String[] USERS = {"kasumi", "arisa", "tae", "rimi", "saaya", "yukina", "sayo", "risa", "rinko", "ako"};
    UserManager userManager;
    TransactionGen transGen;
    SimpleVerifier verifier;
    SM2 sm2;
    
    @Before
    public void setUpSM2() {
        sm2 = SM2Generator.getInstance().generateSM2();
    }
    @Before
    public void setupUserManager() {
        userManager = new UserManager(USERS);
    }
    @Before
    public void setupTransactionGen() {
        transGen = new TransactionGen(userManager);
    }
    @Before
    public void setSimpleVerifier() {
        verifier = new SimpleVerifier(userManager);
    }

    void collectTime(List<Long> timeList, String name) {
        var count = timeList.stream().reduce(Long::sum).orElse(0L);
        System.out.printf("%s总计运行时间: %f s \n", name, (count / 1000000000f));
        System.out.printf("%s平均运行时间: %f s \n", name, count / timeList.size() / 1000000000f);
    }
}
