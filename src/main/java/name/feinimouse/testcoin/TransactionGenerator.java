package name.feinimouse.testcoin;

import name.feinimouse.testcoin.crypt.SM2;
import name.feinimouse.testcoin.crypt.SM2KeyPair;

import java.util.*;

/**
 * Create by 菲尼莫斯 on 2019/6/22
 * Email: cyhkkha@gmail.com
 * File name: TransactionGenerator
 * Program : feinicoin
 * Description :
 */
public class TransactionGenerator {
    private static Map<String, SM2KeyPair> keyMaps = new HashMap<>();
    private static Random random = new Random();
    private static SM2 sm2;
    private static String[] users;
    private static TransactionGenerator generator;
    private static List<Long> generateTimes= new ArrayList<>();


    public static TransactionGenerator getGenerator() throws NullPointerException {
        if (generator == null) {
            throw new NullPointerException("please init generator first");
        }
        return generator;
    }
    
    public static TransactionGenerator init(String[] users, SM2 sm2) {
        if (generator == null) {
            generator = new TransactionGenerator(users, sm2);
        }
        return generator;
    }
    
    private TransactionGenerator(String[] users, SM2 sm2) {
        TransactionGenerator.sm2 = sm2;
        TransactionGenerator.users = users;
        // 为所有用户生成密钥对
        Arrays.stream(users).forEach(name -> keyMaps.put(name, sm2.generateKeyPair()));
    }
    
    // 随机用户
    private String randomUser() {
        // nextInt(n) 生成一个0-n之间的随机int
        return users[random.nextInt(users.length)];
    }
    // 不重复随机用户
    private String randomUser(String user) {
        var u = randomUser();
        if (!u.equals(user)) {
            return u;
        }
        return randomUser(user);
    }
    
    // 签名并统计签名时间
    public TransactionTest signTransactionWithTime(TransactionTest t) {
        var keys = keyMaps.get(t.getSender());
        
        var before = System.currentTimeMillis();
        
        var sign = sm2.sign(t.getSummary(), t.getSender(), keys);
        
        // 统计生成时间
        var finish = System.currentTimeMillis();
        generateTimes.add(finish - before);
        
        var signatureTest = new SignTest();
        signatureTest.setSenderSign(sign);
        t.setSign(signatureTest);
        return t;
    }
    
    // 不统计签名时间的签名
    public TransactionTest signTransaction(TransactionTest t) {
        var sign = sm2.sign(t.getSummary(), t.getSender(), keyMaps.get(t.getSender()));
        var signatureTest = new SignTest();
        signatureTest.setSenderSign(sign);
        t.setSign(signatureTest);
        return t;
    }
    
    // 随机生成一条交易
    public TransactionTest generateTransaction() {
        var sender = randomUser();
        var receiver = randomUser(sender);
        var coin = random.nextInt(1000);
        var time = System.currentTimeMillis();
        var t = new TransactionTest(sender, receiver, coin, time);
        return signTransactionWithTime(t);
    }

    public List<TransactionTest> generateTransactionsNoSign(int count) {
        var list = new ArrayList<TransactionTest>();
        for (var i = count; i > 0; i --) {
            var sender = randomUser();
            var receiver = randomUser(sender);
            var coin = random.nextInt(1000);
            var time = System.currentTimeMillis();
            list.add(new TransactionTest(sender, receiver, coin, time));
        }
        return list;
    }

    public List<Long> getGenerateTimes() {
        return generateTimes;
    }

}
