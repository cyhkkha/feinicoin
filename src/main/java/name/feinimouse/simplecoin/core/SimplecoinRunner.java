package name.feinimouse.simplecoin.core;

import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.feinism2.SM2;
import name.feinimouse.simplecoin.feinism2.SM2Generator;
import name.feinimouse.simplecoin.SimplecoinConfig;
import name.feinimouse.simplecoin.block.SimpleHashObj;
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
    
    // 初始化
    public SimplecoinRunner(SimplecoinConfig simplecoinConfig) {
        TEST_COUNT = simplecoinConfig.TEST_COUNT;
        BUNDLE_SIZE = simplecoinConfig.BUNDLE_SIZE;
        USER_COUNT = simplecoinConfig.USER_COUNT;
        UTXO_SIZE = simplecoinConfig.UTXO_SIZE;
        ASSET_RATE = simplecoinConfig.ASSET_RATE;

        var random = new Random();
        
        // 生成随机用户
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

    // 清除所有运行时间
    public void clear() {
        transGen.getSignTimes().clear();
        verifier.getVerifyTimes().clear();
        verifier.getBundleTimes().clear();
    }
    
    // 简单的用于记录运行时间的函数
    public static long recordTime(ConfigRunner r) {
        var timestart = System.nanoTime();
        r.run();
        return System.nanoTime() - timestart;
    }
    
    // 计算均值并打印一个列表的运行时间
    public static void collectTime(List<Long> timeList, String name) {
        long count = timeList.stream().reduce(Long::sum).orElse(0L);
        System.out.printf("%s次数: %d 次 \n", name, timeList.size());
        System.out.printf("%s总计运行时间: %f ms \n", name, (count / 1000_000f));
        System.out.printf("%s平均运行时间: %f ms \n", name, count / timeList.size() / 1000_000f);
    }
    
    // 将交易列表转化为可存储的document
    public static List<Document> transform(List<Transaction> list) {
        return list.stream().map(t -> new SimpleHashObj(t).toDocument()).collect(Collectors.toList());
    }
    
    // 打印中心的运行数据
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void collectCenter(SimpleCenter center) {
        System.out.println("---------------------------");
        System.out.println(center.getName());
        System.out.printf("共出块：%d 个\n", center.getBlockCounts());
        System.out.printf("运行总时间：%f s\n", center.getRunTime() / 1000_000_000f);
        System.out.printf("验证总时间：%f s\n", center.getVerifyTime() / 1000_000_000f);
        collectTime(center.getSaveTimes(), "出块");
        System.out.println("---------------------------");
    }
    
    // 预运行，让数据库和验签初始化
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
