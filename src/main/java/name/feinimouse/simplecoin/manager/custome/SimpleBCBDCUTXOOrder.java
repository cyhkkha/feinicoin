package name.feinimouse.simplecoin.manager.custome;

import lombok.NonNull;
import name.feinimouse.simplecoin.account.UTXOBundle;
import name.feinimouse.simplecoin.core.UserManager;
import name.feinimouse.simplecoin.manager.SimpleOrder;

import java.util.List;

public class SimpleBCBDCUTXOOrder extends SimpleUTXOOrder {
    public SimpleBCBDCUTXOOrder(@NonNull UserManager manager, @NonNull List<UTXOBundle> list) {
        super(manager, list);
    }

    @Override
    protected void waitOutBlock() {
        
    }
}
