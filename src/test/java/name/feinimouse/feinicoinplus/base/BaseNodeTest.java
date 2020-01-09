package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.utils.LoopUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseNodeTest extends BaseTest {
    @Autowired
    SignGenerator signGenerator;

    @Autowired
    Verifier verifier;
    @Autowired
    Order order;
    @Autowired
    Center center;

    @Test
    public void testVerifier() throws InterruptedException, BadRequestException {
        order.start();
        verifier.start();
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            Packer packer = transactionGenerator.genRandomTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, order.getAddress());
            order.commit(carrier);
            Thread.yield();
        }
        Thread.sleep(8 * 1000);
    }

}
