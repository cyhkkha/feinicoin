package name.feinimouse.feinicoin.account;

/**
 * Create by 菲尼莫斯 on 2019/5/23
 * Email: cyhkkha@gmail.com
 * File name: QuotePreBlockExtFunc
 * Program : feinicoin
 * Description :
 */
public interface QuotePreBlockExtFunc extends ExtFunc {
    /**
     * 最近的一个引用的区块信息，为防止交易在分叉上被重复广播
     * 声明该笔交易在某个分叉上生效
     */
    long getPreBlockNum();
}
