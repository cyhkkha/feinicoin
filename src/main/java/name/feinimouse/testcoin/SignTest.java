package name.feinimouse.testcoin;

import lombok.Data;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.testcoin.crypt.SM2Signature;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: SignTest
 * Program : feinicoin
 * Description :
 */
@Data
public class SignTest implements Sign {
    private SM2Signature senderSign;
    private SM2Signature verifierSign;
    private SM2Signature orderSign;
}
