package name.feinimouse.simplecoin.block;

@SuppressWarnings("rawtypes")
public interface Block {
    MerkelTree getAccounts();
    
    MerkelTree getAssets();

    MerkelTree getTransactions();
    
    Header getHeader();
}