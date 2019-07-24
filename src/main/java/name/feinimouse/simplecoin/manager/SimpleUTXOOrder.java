package name.feinimouse.simplecoin.manager;

import lombok.Getter;
import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.UTXOBundle;
import name.feinimouse.simplecoin.UserManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleUTXOOrder extends SimpleOrder<UTXOBundle, Transaction> {
    public SimpleUTXOOrder(@NonNull UserManager manager, @NonNull List<UTXOBundle> list) {
        super(manager, list);
    }

    private AtomicBoolean isOutBlock = new AtomicBoolean(true);

    public void isOutBlock(boolean bool) {
        isOutBlock.set(bool);
    }
    
    @Override
    public long activate() {
        processing = true;
        verifyTimes.clear();
        UTXOBundle utxo;
        while ((utxo = allTrans.poll()) != null) {
            waitOutBlock();
            utxo.forEach(trans -> {
                if (super.verify(trans)) {
                    orderQueue.add(trans);
                } else {
                    throw new RuntimeException("交易验证失败");
                }
            });
        }
        return verifyTimes.stream().reduce(Long::sum).orElse(0L);
        
    }
}
