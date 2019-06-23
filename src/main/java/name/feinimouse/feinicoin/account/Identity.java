package name.feinimouse.feinicoin.account;

/**
 * Create by 菲尼莫斯 on 2019/5/24
 * Email: cyhkkha@gmail.com
 * File name: Identity
 * Program : feinicoin
 * Description :
 */
public interface Identity {
    default String getSummary() {
        return "default";
    }
}
