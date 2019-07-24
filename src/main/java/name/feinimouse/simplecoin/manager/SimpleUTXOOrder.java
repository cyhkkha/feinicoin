package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.simplecoin.UTXOBundle;
import name.feinimouse.simplecoin.UserManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleUTXOOrder extends SimpleOrder<UTXOBundle, UTXOBundle> {
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
            utxo.forEach(trans -> {
                waitOutBlock();
                if (!super.verify(trans)) {
                    throw new RuntimeException("交易验证失败");
                }
            });
            orderQueue.add(utxo);
        }
        return verifyTimes.stream().reduce(Long::sum).orElse(0L);
        
    }
}
