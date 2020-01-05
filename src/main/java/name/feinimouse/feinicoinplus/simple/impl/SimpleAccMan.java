package name.feinimouse.feinicoinplus.simple.impl;

import name.feinimouse.feinicoinplus.core.data.Account;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAccMan implements AccountManager {
    private List<String> accountList;
    private Map<String, Account> accountMap;

    private AddressManager addressManager;

    private Random random = new Random();

    public SimpleAccMan() {
        accountList = Collections.synchronizedList(new ArrayList<>());
        accountMap = new ConcurrentHashMap<>();
    }

    public SimpleAccMan(AddressManager addressManager) {
        this();
        this.addressManager = addressManager;
    }
    
    @Override
    public void genAccount(int number) {
        for (int i = 0; i < number; i++) {
            String address = addressManager.getAddress();
            int coin = random.nextInt(100) + 500;
            Account account = new Account(address, coin);
            put(account);
        }
    }
    @Override
    public Account getRandom() {
        int index = random.nextInt(size());
        String address = accountList.get(index);
        return get(address);
    }
    @Override
    public Account getRandomEx(Account account) {
        Account accountNext = getRandom();
        while (account == accountNext) {
            accountNext = getRandom();
        }
        return accountNext;
    }
    @Override
    public Account get(String address) {
        return accountMap.get(address);
    }
    @Override
    public boolean contain(String address) {
        return accountMap.containsKey(address);
    }
    @Override
    public synchronized boolean put(Account account) {
        String address = account.getAddress();
        if (contain(address)) {
            return false;
        }
        accountMap.put(address, account);
        accountList.add(address);
        return true;
    }
    @Override
    public int size() {
        return accountList.size();
    }
    @Override
    public synchronized boolean remove(String address) {
        if (contain(address)) {
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
