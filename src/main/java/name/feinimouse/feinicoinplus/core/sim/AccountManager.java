package name.feinimouse.feinicoinplus.core.sim;


import name.feinimouse.feinicoinplus.core.data.Account;

public interface AccountManager {
    void genAccount(int number);
    boolean put(Account account);
    boolean contain(String address);
    Account get(String address);
    Account getRandom();
    Account getRandomEx(Account account);
    int size();
    boolean remove(String address);
}
