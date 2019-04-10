package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Account;

public interface AccountTree {
    public String getHeader();
    public void addAccount(Account a);
    public Account getAccount(String hash);
    public int searchAccount(String hash);
}