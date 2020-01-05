package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.Account;
import name.feinimouse.feinicoinplus.core.data.PackerArr;
import name.feinimouse.feinicoinplus.core.data.Transaction;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component("accountManager")
public class ListMapAccMan implements AccountManager {
    
    protected final HashGenerator hashGenerator;
    protected final AddressManager addressManager;
    
    private List<String> accountList;
    private Map<String, Account> accountMap;


    private Random random = new Random();

    @Autowired
    public ListMapAccMan(HashGenerator hashGenerator, AddressManager addressManager) {
        this.hashGenerator = hashGenerator;
        this.addressManager = addressManager;
        accountList = Collections.synchronizedList(new ArrayList<>());
        accountMap = new ConcurrentHashMap<>();
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

    @Override
    public PackerArr pack() {
        Account[] accounts = accountList.stream().map(accountMap::get).toArray(Account[]::new);
        return hashGenerator.hash(accounts, Account.class);
    }

    @Override
    public boolean commit(Transaction trans) {
        String sender = trans.getSender();
        String receiver = trans.getReceiver();
        if (contain(sender) && contain(receiver)) {
            Account senderAcc = get(trans.getSender());
            Account receiverAcc = get(trans.getReceiver());
            int coin = trans.getNumber();
            senderAcc.setCoin(senderAcc.getCoin() - coin);
            receiverAcc.setCoin(receiverAcc.getCoin() + coin);
            return true;
        }
        return false;
    }
}
