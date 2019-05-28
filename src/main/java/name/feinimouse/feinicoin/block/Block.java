package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Assets;
import name.feinimouse.feinicoin.account.Transaction;

public class Block {
    // 区块编号
    private long number;
    private Header header;
    private MerkelTree<Account> accounts;
    private MerkelTree<Assets> properties;
    private MerkelTree<Transaction> transactions;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public MerkelTree<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(MerkelTree<Account> accounts) {
        this.accounts = accounts;
    }

    public MerkelTree<Assets> getProperties() {
        return properties;
    }

    public void setProperties(MerkelTree<Assets> properties) {
        this.properties = properties;
    }

    public MerkelTree<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(MerkelTree<Transaction> transactions) {
        this.transactions = transactions;
    }
}