package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.account.UTXOBundle;
import name.feinimouse.simplecoin.manager.SimpleCenter;

public class SimpleBCBDCUTXOCenter extends SimpleUTXOCenter {
    
    public SimpleBCBDCUTXOCenter(@NonNull SimpleBCBDCUTXOOrder order) {
        super(order);
        setName("UTXO并行(BCBDC)模式");
    }
}
