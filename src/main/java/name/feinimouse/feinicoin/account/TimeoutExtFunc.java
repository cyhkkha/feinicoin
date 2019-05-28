package name.feinimouse.feinicoin.account;

/**
 * Create by 菲尼莫斯 on 2019/5/23
 * Email: cyhkkha@gmail.com
 * File name: TimeoutExtFunc
 * Program : feinicoin
 * Description :
 */
public interface TimeoutExtFunc extends ExtFunc {
    // 用于标示在一定时间后过期，若数据在一定时间内没有上链，**处理节点**将抛弃该数据
    long getTimeout();
}
