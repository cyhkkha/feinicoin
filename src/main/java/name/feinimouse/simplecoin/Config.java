package name.feinimouse.simplecoin;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.simplecoin.block.SimpleHashObj;
import name.feinimouse.simplecoin.core.TransactionGen;
import name.feinimouse.simplecoin.core.UserManager;
import name.feinimouse.simplecoin.manager.SimpleVerifier;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Config {
    protected final static String[] USERS = {"kasumi", "arisa", "tae", "rimi", "saaya", "yukina", "sayo", "risa", "rinko", "ako"};
    protected static UserManager userManager;
    protected static TransactionGen transGen;
    protected static SimpleVerifier verifier;
    protected static SM2 sm2;

    public static void init() {
        sm2 = SM2Generator.getInstance().generateSM2();
        userManager = new UserManager(USERS);
        transGen = new TransactionGen(userManager);
        verifier = new SimpleVerifier(userManager);
    }

    public static void clear() {
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
    
    public interface ConfigRunner {
        void run();
    }
}
