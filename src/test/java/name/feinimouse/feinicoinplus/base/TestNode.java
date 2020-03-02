package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.utils.LoopUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestNode extends BaseTest {
    
    @Autowired
    SignGenerator signGenerator;

    @Autowired
    Verifier verifier;
    @Autowired
    Order order;
    @Autowired
    FetchCenter fetchCenter;
    @Autowired
    ClassicalCenter classicalCenter;

    @Before
    public void setupNode() {
        order.setVerifiersAddress(verifier.getAddress());
        fetchCenter.setOrdersAddress(order.getAddress());
    }
    
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
    public void testCenter() throws Exception {
        order.start();
        verifier.start();
        fetchCenter.start();
        String orderAddress = order.getAddress();
        Thread.sleep(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            Packer packer = transactionGenerator.genRandomTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, orderAddress);
            LoopUtils.loopExec(50, 10, () -> order.commit(carrier));
        }
        logger.info("交易发送完毕，运行时间 {} ms", System.currentTimeMillis() - start);
        order.join();
        verifier.join();
        fetchCenter.join();
        logger.info("总计运行时间 {} ms", System.currentTimeMillis() - start);
    }
    
    @Test
    public void testClassicalCenter() throws Exception {
        classicalCenter.start();
        String centerAddress = classicalCenter.getAddress();
        Thread.sleep(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            Packer packer = transactionGenerator.genRandomTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, centerAddress);
            carrier.getNetInfo().setMsgType(Node.MSG_COMMIT_CENTER);
            LoopUtils.loopExec(50, 10, () -> classicalCenter.commit(carrier));
        }
        logger.info("交易发送完毕，运行时间 {} ms", System.currentTimeMillis() - start);
        classicalCenter.join();
        logger.info("总计运行时间 {} ms", System.currentTimeMillis() - start);
    }

    @Test
    public void testCenterAsset() throws Exception {
        order.start();
        verifier.start();
        fetchCenter.start();
        String orderAddress = order.getAddress();
        Thread.sleep(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            Packer packer = transactionGenerator.genRandomAssetTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, orderAddress);
            LoopUtils.loopExec(50, 10, () -> order.commit(carrier));
        }
        logger.info("交易发送完毕，运行时间 {} ms", System.currentTimeMillis() - start);
        order.join();
        verifier.join();
        fetchCenter.join();
        logger.info("总计运行时间 {} ms", System.currentTimeMillis() - start);
    }

    @Test
    public void testClassicalCenterAsset() throws Exception {
        classicalCenter.start();
        String centerAddress = classicalCenter.getAddress();
        Thread.sleep(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            Packer packer = transactionGenerator.genRandomAssetTrans();
            Carrier carrier = transactionGenerator.genCarrier(packer, centerAddress);
            carrier.getNetInfo().setMsgType(Node.MSG_COMMIT_CENTER);
            LoopUtils.loopExec(50, 10, () -> classicalCenter.commit(carrier));
        }
        logger.info("交易发送完毕，运行时间 {} ms", System.currentTimeMillis() - start);
        classicalCenter.join();
        logger.info("总计运行时间 {} ms", System.currentTimeMillis() - start);
    }
    
    @After
    public void after() {
        order.stopNode();
        verifier.stopNode();
        fetchCenter.stopNode();
    }

}
