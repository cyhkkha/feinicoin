package name.feinimouse.simplecoin.core;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.simplecoin.block.SimpleHashObj;
import name.feinimouse.simplecoin.core.TransactionGen;
import name.feinimouse.simplecoin.core.UserManager;
import name.feinimouse.simplecoin.manager.SimpleCenter;
import name.feinimouse.simplecoin.manager.SimpleVerifier;
import name.feinimouse.simplecoin.mongodao.MongoDao;
import name.feinimouse.simplecoin.mongodao.TransDao;
import name.feinimouse.utils.LoopUtils;
import org.bson.Document;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class SimplecoinRunner {
    protected UserManager userManager;
    protected TransactionGen transGen;
    protected SimpleVerifier verifier;
    protected SM2 sm2;

    protected Integer[] TEST_COUNT;
    protected int BUNDLE_SIZE;
    protected int UTXO_SIZE;
    protected double ASSET_RATE;
    protected int USER_COUNT;
    
    public SimplecoinRunner(Config config) {
        TEST_COUNT = config.getTEST_COUNT();
        BUNDLE_SIZE = config.getBUNDLE_SIZE();
        USER_COUNT = config.getUSER_COUNT();
        UTXO_SIZE = config.getUTXO_SIZE();
        ASSET_RATE = config.getASSET_RATE();

        var random = new Random();
        var USERS = LoopUtils.loopToList(USER_COUNT, () -> {
            var l = random.nextInt(1000) + System.nanoTime();
            return String.valueOf(l);
        });
        sm2 = SM2Generator.getInstance().generateSM2();
        userManager = new UserManager(USERS.toArray(String[]::new));
        transGen = new TransactionGen(userManager);
        verifier = new SimpleVerifier(userManager);
    }
    
    public abstract StatisticsObj run();

    public void clear() {
        transGen.getSignTimes().clear();
        verifier.getVerifyTimes().clear();
        verifier.getBundleTimes().clear();
    }
    
    public static long recordTime(ConfigRunner r) {
        var timestart = System.nanoTime();
        r.run();
        return System.nanoTime() - timestart;
    }
    
    public static void collectTime(List<Long> timeList, String name) {
        var count = timeList.stream().reduce(Long::sum).orElse(0L);
        System.out.printf("%s次数: %d 次 \n", name, timeList.size());
        System.out.printf("%s总计运行时间: %f ms \n", name, (count / 1000_000f));
        System.out.printf("%s平均运行时间: %f ms \n", name, count / timeList.size() / 1000_000f);
    }
    
    public static List<Document> transform(List<Transaction> list) {
        return list.stream().map(t -> new SimpleHashObj(t).toDocument()).collect(Collectors.toList());
    }
    
    @SuppressWarnings("unchecked")
    public static void collectCenter(SimpleCenter center) {
        System.out.println("---------------------------");
        System.out.println(center.getName());
        System.out.printf("共出块：%d 个\n", center.getBlockCounts());
        System.out.printf("运行总时间：%f s\n", center.getRunTime() / 1000_000_000f);
        System.out.printf("验证总时间：%f s\n", center.getVerifyTime() / 1000_000_000f);
        collectTime(center.getSaveTimes(), "出块");
        System.out.println("---------------------------");
    }
    
    public void preRun() {
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
    }
    
    public interface ConfigRunner {
        void run();
    }
}
