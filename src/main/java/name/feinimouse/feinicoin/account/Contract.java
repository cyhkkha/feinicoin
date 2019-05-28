package name.feinimouse.feinicoin.account;

import name.feinimouse.feinicoin.block.Hashable;

/**
 * Create by 菲尼莫斯 on 2019/5/23
 * Email: cyhkkha@gmail.com
 * File name: Contract
 * Program : feinicoin
 * Description :
 */
public interface Contract extends Hashable {
    Object run(String key, String[] args);
    String[] getOwner();
    String getDescription();
}
