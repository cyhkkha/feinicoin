package name.feinimouse.simplecoin.core.impl;

import name.feinimouse.simplecoin.SimplecoinConfig;
import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.StatisticsObj;
import name.feinimouse.simplecoin.mongodao.MongoDao;
import name.feinimouse.simplecoin.mongodao.TransDao;
import name.feinimouse.utils.LoopUtils;

import java.util.Collections;
import java.util.LinkedList;

public class BaseInfoRunner extends SimplecoinRunner {
    private static final int TRANS_SIZE = 1000;
    private static final int UTXO_SIZE = 10;
    private final static int USER_COUNT = 100;

    public BaseInfoRunner() {
        super(new SimplecoinConfig(USER_COUNT));
    }

    @Override
    public StatisticsObj run() {
        return null;
    }

    public void showInfo() {
        preRun();

        var msg = MongoDao.createNewBlock();
        var number = msg.getInteger("number");
        
        var transList = LoopUtils.loopToList(TRANS_SIZE, transGen::genSignedTransFa);
        collectTime(transGen.getSignTimes(), "签名");
        transList.forEach(t -> verifier.verify(t));
        collectTime(verifier.getVerifyTimes(), "验签");
        verifier.bundle(transList);
        collectTime(verifier.getBundleTimes(), "打包");
        clear();
        
        var utxoVerifyList = new LinkedList<Long>();
        var utxoList = LoopUtils.loopToList(20, () -> transGen.genUTXOBundle(UTXO_SIZE));
        utxoList.forEach(utxo -> {
            utxo.forEach(t -> verifier.verify(t, utxo.getOwner()));
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
