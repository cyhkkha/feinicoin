package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.custome.SimpleUTXOCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleUTXOOrder;
import name.feinimouse.utils.LoopUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class TestUTXO extends TestCenter<UTXOBundle> {
    private final static int LIST_SIZE = 100;
    private final static int UTXO_SIZE = 5;

    @Before @Override
    public void setUp() {
        sourceList = LoopUtils.loopToList(LIST_SIZE, () -> transGen.genUTXOBundle(UTXO_SIZE));
        var order = new SimpleUTXOOrder(userManager, sourceList);
        center = new SimpleUTXOCenter(order);
        order.isOutBlock(false);
        super.order = order;
    }
    
    @Test @Override
    public void testOrder() throws ExecutionException, InterruptedException {
        var executor = Executors.newSingleThreadExecutor();
        var orderRes = executor.submit(order::activate);
        Thread.sleep(500);
        var verifyTime = orderRes.get();
        System.out.printf("验证 %d 条UTXO共花费：%f s \n", sourceList.size(), verifyTime / 1000000000f);
    }

    @Test
    public void testWrite() {
        write();
    }

    @Test
    public void testCenter() {
        runCenter();
    }
}
