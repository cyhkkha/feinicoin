package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
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
    public void testVerifier() throws InterruptedException {
        order.start();
        verifier.start();
        Thread.sleep(1000);
        LoopUtils.loop(1, () -> {
            Packer packer = transactionGenerator.genRandomTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, order.getAddress());
            try {
                order.commit(carrier);
            } catch (BadCommitException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(8 * 1000);
    }
    
}
