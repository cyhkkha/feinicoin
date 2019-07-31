package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.account.MixedBundle;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCCenter;
import name.feinimouse.simplecoin.manager.custome.SimpleMixedBCBDCOrder;
import name.feinimouse.utils.LoopUtils;
import org.junit.Before;
import org.junit.Test;

public class TestMixedBCBDC extends TestCenter<MixedBundle> {
    private final static int TRANS_SIZE = 100;
    private final static int ASSET_SIZE = 20;
    private final static int BUNDLE_SIZE = 10;
    private final static int UTXO_SIZE = 5;

    @Before
    @Override
    public void setUp() {
        sourceList = LoopUtils.loopToList(TRANS_SIZE, () -> transGen.genMixedBundle());
        LoopUtils.loop(ASSET_SIZE, () -> sourceList.add(transGen.genMixedBundle(UTXO_SIZE)));
        var order = new SimpleMixedBCBDCOrder(userManager, sourceList);
        order.setBundleLimit(BUNDLE_SIZE);
        center = new SimpleMixedBCBDCCenter(order);
        super.order = order;
    }

    @Test
    @Override
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
