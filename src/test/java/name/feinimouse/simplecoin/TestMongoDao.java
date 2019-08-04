package name.feinimouse.simplecoin;

import name.feinimouse.feinicoin.account.Assets;
import name.feinimouse.simplecoin.block.SimpleHashObj;
import name.feinimouse.simplecoin.mongodao.AccountDao;
import name.feinimouse.simplecoin.mongodao.AssetsDao;
import name.feinimouse.simplecoin.mongodao.MongoDao;
import name.feinimouse.simplecoin.mongodao.TransDao;
import name.feinimouse.utils.LoopUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestMongoDao extends SetupTest {
    
    @Test
    public void testInsert() {
//        var list = LoopUtils.loopToList(10, transGen::genSignedTrans);
//        list.forEach(item -> MongoDao.insertTest(Document.parse(item.getSummary())));
        
        var trans = transGen.genSignedTrans();
        var before = System.nanoTime();
        MongoDao.insertTest(Document.parse(trans.getSummary()));
        var consume = System.nanoTime() - before;
        System.out.printf("插入数据耗时: %f s", consume / 1000_000_000f);
    }
    @Test
    public void batchInsert() {
//        var list = LoopUtils.loopToList(10, transGen::genSignedTrans);
//        list.forEach(item -> MongoDao.insertTest(Document.parse(item.getSummary())));
        
        var list = LoopUtils.loopToList(100, transGen::genSignedTrans);
        var before = System.nanoTime();
        list.forEach(item -> MongoDao.insertTest(Document.parse(item.getSummary())));
        var consume = System.nanoTime() - before;
        System.out.printf("批量插入数据耗时: %f s", consume / 1000_000_000f);
    }

    @Test
    public void testPullDB() {
        var list = TransDao.getList(3);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }
    
    @Test
    public void testUpdate() {
        var header = new Document("testdata", "ssss");
        MongoDao.insertHeader(5, header);
    }
    
    @Test
    public void testDrop() {
        MongoDao.dropTest();
        TransDao.drop();
        AccountDao.drop();
        AssetsDao.drop();
    }
    
    private List<Document> loopADocument(int i) {
        return LoopUtils.loopToList(i, () -> {
            var trans = transGen.genSignedTrans();
            return new SimpleHashObj(trans).toDocument();
        });
    }
    
    @Test
    public void testInsertTime() {
        var tempList = loopADocument(10);
        var msg = MongoDao.createNewBlock();
        var number = msg.getInteger("number");
        TransDao.insertList(number, tempList);
        var timeList = new LinkedList<Long>();
        LoopUtils.loop(20, () -> {
            var transList = loopADocument(1000);
            var timestart = System.nanoTime();
            TransDao.insertList(number, transList);
            var time = System.nanoTime() - timestart;
            timeList.add(time);
        });
        collectTime(timeList, "插入");
    }
    
    @Test
    public void testQueryTime() {
        var tempList = loopADocument(1000);
        var msg = MongoDao.createNewBlock();
        var number = msg.getInteger("number");
        TransDao.insertList(number, tempList);
        tempList = TransDao.getList(number);
        Assert.assertTrue(tempList.size() > 0);
        System.out.printf("总共找到 %d 条数据 \n", tempList.size());
        var timeList = new LinkedList<Long>();
        LoopUtils.loop(20, () -> {
            var timestart = System.nanoTime();
            TransDao.getList(number);
            var time = System.nanoTime() - timestart;
            timeList.add(time);
        });
        collectTime(timeList, "取出");
    }
    
}
