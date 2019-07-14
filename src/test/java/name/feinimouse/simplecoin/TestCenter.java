package name.feinimouse.simplecoin;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.simplecoin.block.SimpleBlock;
import name.feinimouse.simplecoin.block.SimpleHashObj;
import name.feinimouse.simplecoin.block.SimpleHeader;
import name.feinimouse.simplecoin.block.SimpleMerkelTree;
import name.feinimouse.simplecoin.manager.SimpleUTXOCenter;
import name.feinimouse.simplecoin.manager.SimpleUTXOOrder;
import name.feinimouse.utils.LoopUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.security.SignatureException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TestCenter extends SetupTest {
    private SimpleUTXOCenter center;
    private SimpleUTXOOrder order;
    private List<Transaction> transList;
    private SimpleHeader testHeader;
    
    @Before
    public void setUp() throws SignatureException {
        transList = LoopUtils.loopToList(1000, transGen::genSignedTrans);
        order = new SimpleUTXOOrder(userManager, transList);
        center = new SimpleUTXOCenter(order);

        // 生成区块头
        var summary = new JSONObject();
        summary.put("number", 0)
            .put("preHash", "0000000000")
            .put("timestamp", System.currentTimeMillis())
            .put("producer", "Simple Center")
            .put("transRoot", "xxxxxxxx")
            .put("assetRoot", "xxxxxxxx")
            .put("accountRoot", "xxxxxxxx")
            .put("version", "0.0.1");
        testHeader = new SimpleHeader(summary);
        var sign = new SimpleSign();
        sign.setSign("center", sm2.signToByte(summary.toString()));
        testHeader.setSign(sign);
    }
    
    @Test
    public void testWrite() {
        var merkelList = transList.stream()
            .map(t -> new SimpleHashObj(
                t.getSummary(),
                t.getHash(),
                t.getSign()
            ))
            .collect(Collectors.toList());
        var tree = new SimpleMerkelTree<>(merkelList);
        var block = new SimpleBlock(tree, tree, tree, testHeader);
        center.write(block);
    }
    
    @Test
    public void testCollect() throws ExecutionException, InterruptedException {
        var executor = Executors.newFixedThreadPool(2);
        var orderRes = executor.submit(order::activate);
        var centerRes = executor.submit(center::collectTransaction);
        centerRes.get();
        var verifyTimes = orderRes.get();
        System.out.printf("验证 %d 条交易共花费：%f s \n", transList.size(), verifyTimes / 1000000000f);
        System.out.printf("共解析交易 %d 条 \n", center.getCacheTransList().size());
    }
    
    @Test
    public void testCenter() {
        center.activate();
        System.out.printf("运行总时间：%f \n", center.getRunTime() / 1000000000f);
        System.out.printf("验证总时间：%f \n", center.getVerifyTime() / 1000000000f);
        super.collectTime(center.getSaveTimes(), "写入");
    }
    
}
