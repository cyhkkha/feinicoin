package name.feinimouse.feinicoin.account;

/**
 * Create by 菲尼莫斯 on 2019/5/20
 * Email: cyhkkha@gmail.com
 * File name: ExtFunc
 * Program : feinicoin
 * Description :
 */
public interface ExtFunc {
    default int length() {
        return 0;
    }
    String get();
}
