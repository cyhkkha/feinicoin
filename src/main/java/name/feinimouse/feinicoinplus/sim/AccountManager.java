package name.feinimouse.feinicoinplus.sim;


import name.feinimouse.feinicoinplus.core.block.Account;

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
