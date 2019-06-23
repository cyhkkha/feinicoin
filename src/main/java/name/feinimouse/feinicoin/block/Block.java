package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Assets;
import name.feinimouse.feinicoin.account.Transaction;

public interface Block {
    MerkelTree<Account> getAccounts();
    
    MerkelTree<Assets> getAssets();

    MerkelTree<Transaction> getTransactions();
    
    // 区块编号
    long getNumber();
    
    Header getHeader();
}