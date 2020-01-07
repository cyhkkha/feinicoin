package name.feinimouse.feinicoinplus.core.sim;


import name.feinimouse.feinicoinplus.core.data.Account;
import name.feinimouse.feinicoinplus.core.data.PackerArr;
import name.feinimouse.feinicoinplus.core.data.Transaction;

import java.security.PrivateKey;

public interface AccountManager {
    void genAccount(int number);
    boolean put(Account account);
    boolean contain(String address);
    Account get(String address);
    PrivateKey getPrivateKey(String address);
    Account getRandom();
    Account getRandomEx(Account account);
    Account getRandomEx(String address);
    int size();
    boolean remove(String address);

    PackerArr pack();
    boolean commit(Transaction transaction);
}
