package name.feinimouse.simplecoin.block;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public class SimpleBlock implements Block {
    @Getter @Setter
    private SimpleMerkelTree accounts;
    @Getter @Setter
    private SimpleMerkelTree assets;
    @Getter @Setter
    private SimpleMerkelTree transactions;
    @Getter @Setter
    private SimpleHeader header;
    
    public SimpleBlock(
        SimpleMerkelTree accounts,
        SimpleMerkelTree assets,
        SimpleMerkelTree transactions,
        SimpleHeader header) {
        this.accounts = accounts;
        this.assets = assets;
        this.transactions = transactions;
        this.header = header;
    }
    
}
