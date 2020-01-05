package name.feinimouse.feinicoinplus.sim;


import name.feinimouse.feinicoinplus.core.block.Account;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {
    private List<String> accountList;
    private Map<String, Account> accountMap;

    private AddressManager addressManager;

    private Random random = new Random();

    public AccountManager() {
        accountList = Collections.synchronizedList(new ArrayList<>());
        accountMap = new ConcurrentHashMap<>();
    }

    public AccountManager(AddressManager addressManager) {
        this();
        this.addressManager = addressManager;
    }

    public void genAccount(int number) {
        for (int i = 0; i < number; i++) {
            String address = addressManager.getAddress();
            int coin = random.nextInt(100) + 500;
            Account account = new Account(address, coin);
            putAccount(account);
        }
    }

    public Account getRanAccount() {
        int index = random.nextInt(size());
        String address = accountList.get(index);
        return getAccount(address);
    }

    public Account getRanAccountEx(Account account) {
        Account accountNext = getRanAccount();
        while (account == accountNext) {
            accountNext = getRanAccount();
        }
        return accountNext;
    }

    public Account getAccount(String address) {
        return accountMap.get(address);
    }

    public boolean containAccount(String address) {
        return accountMap.containsKey(address);
    }

    @SuppressWarnings("UnusedReturnValue")
    public synchronized boolean putAccount(Account account) {
        String address = account.getAddress();
        if (containAccount(address)) {
            return false;
        }
        accountMap.put(address, account);
        accountList.add(address);
        return true;
    }

    public int size() {
        return accountList.size();
    }

    public synchronized boolean removeAccount(String address) {
        if (containAccount(address)) {
            int index = accountList.indexOf(address);
            if (index >= 0) {
                accountList.remove(index);
            }
            accountMap.remove(address);
            return true;
        }
        return false;
    }
}
