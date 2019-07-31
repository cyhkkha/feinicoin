package name.feinimouse.simplecoin;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.manager.SimplePureAccountCenter;
import name.feinimouse.simplecoin.manager.SimplePureAccountOrder;
import name.feinimouse.utils.LoopUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

public class TestPreAccount extends TestCenter<Transaction> {
    private final static int LIST_SIZE = 100;
    
    @Before @Override
    public void setUp() {
        transList = LoopUtils.loopToList(LIST_SIZE, transGen::genSignedTrans);
        var order = new SimplePureAccountOrder(userManager, transList);
        center = new SimplePureAccountCenter(order);
        order.isOutBlock(false);
        super.order = order;
    }
    
    @Test @Override
    public void testOrder() throws Exception {
        var order = (SimplePureAccountOrder)super.order;
        var startTime = System.nanoTime();
        order.isOutBlock(true);
        var executor = Executors.newSingleThreadExecutor();
        var orderRes = executor.submit(order::activate);
        Thread.sleep(500);
        order.isOutBlock(false);
        Thread.sleep(500); // 这段时间休眠和验证重合了
        order.isOutBlock(true);
        Thread.sleep(500);
        order.isOutBlock(false);
        var verifyTime = orderRes.get();
        var runTime = System.nanoTime() - startTime;
        System.out.printf("验证 %d 条交易共花费：%f s \n", transList.size(), verifyTime / 1000000000f);
        System.out.printf("总运行时间：%f s \n", runTime / 1000000000f);
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
