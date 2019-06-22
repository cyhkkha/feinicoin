package name.feinimouse.crypt;

import java.util.*;

/**
 * Create by 菲尼莫斯 on 2019/6/22
 * Email: cyhkkha@gmail.com
 * File name: Generator
 * Program : feinicoin
 * Description :
 */
public class Generator {
    private static Map<String, SM2KeyPair> keyMaps = new HashMap<>();
    private static Random random = new Random();
    private static SM2 sm2;
    private static String[] users;
    private static Generator generator;
    private static List<Long> generateTimes= new ArrayList<>();
    private static List<Long> verifyTimes= new ArrayList<>();


    public static Generator getGenerator(String[] users, SM2 sm2) {
        if (generator == null) {
            generator = new Generator(users, sm2);
        }
        return generator;
    }
    
    private Generator(String[] users, SM2 sm2) {
        Generator.sm2 = sm2;
        Generator.users = users;
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
    
    public Transaction signTransactionWithTime(Transaction t) {
        var before = System.currentTimeMillis();
        t.setSignature(sm2.sign(t.getSummary(), t.getSender(), keyMaps.get(t.getSender())));
        var finish = System.currentTimeMillis();
        // 统计生成时间
        generateTimes.add(finish - before);
        return t;
    }
    public Transaction signTransaction(Transaction t) {
        t.setSignature(sm2.sign(t.getSummary(), t.getSender(), keyMaps.get(t.getSender())));
        return t;
    }
    
    // 随机生成一条交易
    public Transaction generateTransaction() {
        var sender = randomUser();
        var receiver = randomUser(sender);
        var coin = random.nextInt(1000);
        var time = System.currentTimeMillis();
        var t = new Transaction(sender, receiver, coin, time);
        return signTransactionWithTime(t);
    }
    
    public List<Transaction> generateTransactionsNoSign(int count) {
        var list = new ArrayList<Transaction>();
        for (var i = count; i > 0; i --) {
            var sender = randomUser();
            var receiver = randomUser(sender);
            var coin = random.nextInt(1000);
            var time = System.currentTimeMillis();
            list.add(new Transaction(sender, receiver, coin, time));
        }
        return list;
    }
    
    public boolean verifyTransacation(Transaction t) {
        var before = System.currentTimeMillis();
        var result = sm2.verify(
            t.getSummary(), t.getSignature(), t.getSender(), 
            keyMaps.get(t.getSender()).getPublicKey()
        );
        var finish = System.currentTimeMillis();
        verifyTimes.add(finish - before);
        return result;
    }

    public List<Long> getGenerateTimes() {
        return generateTimes;
    }
}
