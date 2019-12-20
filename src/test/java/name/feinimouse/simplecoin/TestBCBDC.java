package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCAccountCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleBCBDCAccountOrder;
import name.feinimouse.utils.LoopUtils;
import org.junit.Before;
import org.junit.Test;


public class TestBCBDC extends TestCenter<Transaction> {
    private final static int LIST_SIZE = 100;
    private final static int BUNDLE_SIZE = 10;

    @Before @Override
    public void setUp() {
        sourceList = LoopUtils.loopToList(LIST_SIZE, transGen::genSignedTrans);
        var order = new SimpleBCBDCAccountOrder(userManager, sourceList);
        order.setBundleLimit(BUNDLE_SIZE);
        center = new SimpleBCBDCAccountCenter(order);
        super.order = order;
    }
    
    @Test @Override
    public void testOrder() {
        var bundleTime = order.activate();
        System.out.printf("打包 %d 条交易共花费：%f s \n", sourceList.size(), bundleTime / 1000000000f);
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
