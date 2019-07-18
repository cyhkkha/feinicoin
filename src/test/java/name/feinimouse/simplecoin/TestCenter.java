package name.feinimouse.simplecoin;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.block.SimpleHeader;
import name.feinimouse.simplecoin.manager.SimpleCenter;
import name.feinimouse.simplecoin.manager.SimpleOrder;

import java.util.List;

public abstract class TestCenter extends SetupTest {
    SimpleCenter center;
    SimpleOrder order;
    List<Transaction> transList;
    
    public abstract void setUp();
    
    public abstract void testOrder() throws Exception;
    
    public void write() {
        var startTIme = System.nanoTime();

        var verifyTime = order.activate();

        var createTimeStart = System.nanoTime();

        var block = center.createBlock();
        var header = (SimpleHeader)block.getHeader();
        System.out.printf("header: %s \n", header.toJson().toString());
        center.write(block);

        var createTime = System.nanoTime() - createTimeStart;

        var totalTime = System.nanoTime() - startTIme;
        System.out.printf("创建区块耗时：%f s \n", createTime / 1000_000_000f);
        System.out.printf("验证 %d 条交易共花费：%f s \n", transList.size(), verifyTime / 1000_000_000f);
        System.out.printf("总耗时：%f s \n", totalTime / 1000_000_000f);
    }

    @SuppressWarnings("unchecked")
    public void runCenter() {
        center.activate();
        System.out.printf("运行总时间：%f \n", center.getRunTime() / 1000000000f);
        System.out.printf("验证总时间：%f \n", center.getVerifyTime() / 1000000000f);
        super.collectTime(center.getSaveTimes(), "出块");
    }
    
}
