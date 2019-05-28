package name.feinimouse.feinicoin.account;

/**
 * Create by 菲尼莫斯 on 2019/5/23
 * Email: cyhkkha@gmail.com
 * File name: ContractExtFunc
 * Program : feinicoin
 * Description :
 */
public interface ContractExtFunc extends ExtFunc {
    Contract getContract(String contractId);
}
