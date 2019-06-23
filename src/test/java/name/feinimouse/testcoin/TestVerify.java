package name.feinimouse.testcoin;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: TestVerify
 * Program : feinicoin
 * Description :
 */
public class TestVerify extends TestSign {
    VerifierTest verifier1;
    VerifierTest verifier2;
    ArrayList<TransactionTest> transList = new ArrayList<>();
    int transCount = 100;

    @Before
    public void genVerifier() {
        verifier1 = new VerifierTest("阿里", super.users, super.sm2);
        verifier2 = new VerifierTest("腾讯", super.users, super.sm2);
        for (var i = transCount; i > 0; i --) {
            transList.add(super.generator.generateTransaction());
        }
    }
    
    @Test
    public void testVerify() {
        
        for (var t : transList) {
            verifier1.verifyTransactionWithTime(t);
        }
        var times = verifier1.getVerifyTimes();
        super.calculateTime(times,"验签");
    }

    @Test
    public void testVerifyAndSign() throws VerifyFailException {
        for (var t : transList) {
            verifier1.verifyAndSignWithTime(t);
        }
        var times = verifier1.getVerifyTimes();
        super.calculateTime(times, "验签并签名");
    }

}
