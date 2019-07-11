package name.feinimouse.simplecoin.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.Header;
import name.feinimouse.feinicoin.block.MerkelTree;

public class SimpleBlock implements Block {
    @Getter @Setter
    private MerkelTree accounts;
    @Getter @Setter
    private MerkelTree assets;
    @Getter @Setter
    private MerkelTree transactions;
    @Getter @Setter
    private Header header;
    
    public SimpleBlock( MerkelTree accounts, MerkelTree assets, MerkelTree transactions,Header header ) {
        this.accounts = accounts;
        this.assets = assets;
        this.transactions = transactions;
        this.header = header;
    }
    
}
