package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.node.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.node.exception.NodeBusyException;
import org.junit.After;
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
        }
        Thread.sleep(8 * 1000);
    }
    
    @Test
    public void testCenter() throws InterruptedException, BadRequestException {
        order.start();
        verifier.start();
        center.start();
        String orderAddress = order.getAddress();
        Thread.sleep(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            Packer packer = transactionGenerator.genRandomTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, orderAddress);
            try {
                order.commit(carrier);
            } catch (NodeBusyException e) {
                Thread.sleep(50);
                order.commit(carrier);
            }
        }
        logger.info("交易发送完毕，运行时间 {} ms", System.currentTimeMillis() - start);
        order.join();
        verifier.join();
        center.join();
        logger.info("总计运行时间 {} ms", System.currentTimeMillis() - start);
    }
    
    @After
    public void after() {
        order.stopNode();
        verifier.stopNode();
        center.stopNode();
    }

}
