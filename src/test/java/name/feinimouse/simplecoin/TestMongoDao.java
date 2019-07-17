package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.block.MongoDao;
import name.feinimouse.utils.LoopUtils;
import org.bson.Document;
import org.junit.Test;

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
    public void testDrop() {
        MongoDao.dropTest();
    }
    
}
