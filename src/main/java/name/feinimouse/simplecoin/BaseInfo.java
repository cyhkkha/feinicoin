package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.mongodao.MongoDao;
import name.feinimouse.simplecoin.mongodao.TransDao;
import name.feinimouse.utils.LoopUtils;

import java.util.Collections;
import java.util.LinkedList;

public class BaseInfo extends Config {
    private static final int TRANS_SIZE = 1000;
    private static final int UTXO_SIZE = 10;
    
    public static void main(String[] args) {
        init();
        clear();
        
        var firstList = LoopUtils.loopToList(10, transGen::genSignedTransFa);
        collectTime(transGen.getSignTimes(), "第一次签名");
        firstList.forEach(t -> verifier.verify(t));
        collectTime(verifier.getVerifyTimes(), "第一次验证");
        verifier.bundle(firstList);
        collectTime(verifier.getBundleTimes(), "第一次打包");
        clear();
        
        var firstUtxoList = transGen.genUTXOBundle(10);
        firstUtxoList.forEach(t -> verifier.verify(t, firstUtxoList.getOwner()));
        collectTime(Collections.singletonList(verifier.getVerifyTimes().stream()
            .reduce(Long::sum).orElse(0L)), "第一次验UTXO");
        clear();
        
        var msg = MongoDao.createNewBlock();
        var number = msg.getInteger("number");
        TransDao.insertList(number, transform(firstList));
        TransDao.getList(number);
        clear();
        
        var transList = LoopUtils.loopToList(TRANS_SIZE, transGen::genSignedTransFa);
        collectTime(transGen.getSignTimes(), "签名");
        transList.forEach(t -> verifier.verify(t));
        collectTime(verifier.getVerifyTimes(), "验签");
        verifier.bundle(firstList);
        collectTime(verifier.getBundleTimes(), "打包");
        clear();
        
        var utxoVerifyList = new LinkedList<Long>();
        var utxoList = LoopUtils.loopToList(20, () -> transGen.genUTXOBundle(UTXO_SIZE));
        utxoList.forEach(utxo -> {
            utxo.forEach(t -> verifier.verify(t, firstUtxoList.getOwner()));
            utxoVerifyList.add(verifier.getVerifyTimes().stream()
                .reduce(Long::sum).orElse(0L));
            verifier.getVerifyTimes().clear();
        });
        collectTime(utxoVerifyList, "验证utxo");
        clear();
        
        var insertTime = recordTime(() -> TransDao.insertList(number, transform(transList)));
        collectTime(Collections.singletonList(insertTime), "插入");
        var queryTime = recordTime(() -> TransDao.getList(number));
        collectTime(Collections.singletonList(queryTime), "查询");
    }
}
