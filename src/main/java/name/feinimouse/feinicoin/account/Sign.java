package name.feinimouse.feinicoin.account;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: Sign
 * Program : feinicoin
 * Description :
 */
public interface Sign {
    default byte[] getByte(String name) {
        return null;
    }
}
