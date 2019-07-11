package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Assets;
import name.feinimouse.feinicoin.account.Transaction;

public interface Block {
    MerkelTree getAccounts();
    
    MerkelTree getAssets();

    MerkelTree getTransactions();
    
    Header getHeader();
}