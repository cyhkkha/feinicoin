package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Account;
import name.feinimouse.feinicoin.account.Property;
import name.feinimouse.feinicoin.account.Transcation;

public abstract class Block {
    protected Header header;
    protected MerkelTree<Account> accounts;
    protected MerkelTree<Property> propertys;
    protected MerkelTree<Transcation> transcations;
}