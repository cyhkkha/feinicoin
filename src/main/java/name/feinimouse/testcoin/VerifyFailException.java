package name.feinimouse.testcoin;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: VerifyFailException
 * Program : feinicoin
 * Description :
 */
public class VerifyFailException extends Exception {
    public VerifyFailException() {
        super("Transaction verified failed");
    }
    public VerifyFailException(TransactionTest t) {
        super("Transaction: " + t.getSummary() + " verified failed");
    }
    public VerifyFailException(TransactionTest t, String msg) {
        super("Transaction: " + t.getSummary() + " verified failed by: " + msg);
    }
}
