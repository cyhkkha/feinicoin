package name.feinimouse.testcoin;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Verifier;
import name.feinimouse.testcoin.crypt.SM2;
import name.feinimouse.testcoin.crypt.SM2KeyPair;

import java.security.SignatureException;
import java.util.*;

/**
 * Create by 菲尼莫斯 on 2019/6/23
 * Email: cyhkkha@gmail.com
 * File name: VerifierTest
 * Program : feinicoin
 * Description :
 */
public class VerifierTest implements Verifier {
    private Map<String, SM2KeyPair> keyMaps = new HashMap<>();
    private Random random = new Random();
    private SM2 sm2;
    private String[] users;
    private String nodeName;
    private SM2KeyPair nodeKeys;
    private List<Long> verifyTimes= new ArrayList<>();


    public VerifierTest(String name, String[] users, SM2 sm2) {
        this.users = users;
        this.sm2 = sm2;
        this.nodeName = name;
        nodeKeys = sm2.generateKeyPair();
        Arrays.stream(users).forEach(user -> keyMaps.put(user, sm2.generateKeyPair()));
    }
    

    public boolean verifyTransactionWithTime(TransactionTest t) {
        var sign = t.getSign().getSenderSign();
        var publicKey = keyMaps.get(t.getSender()).getPublicKey();
        
        var before = System.currentTimeMillis();
        
        var result = sm2.verify(
            t.getSummary(), sign, t.getSender(), publicKey
        );
        
        // 统计验签时间
        var finish = System.currentTimeMillis();
        verifyTimes.add(finish - before);
        
        return result;
    }

    public boolean verifyTransaction(TransactionTest t) {
        var sign = t.getSign().getSenderSign();
        var publicKey = keyMaps.get(t.getSender()).getPublicKey();
        return sm2.verify(
            t.getSummary(), sign, t.getSender(), publicKey
        );
    }
    
    public TransactionTest verifyAndSign(TransactionTest t) throws VerifyFailException {
        if (verifyTransaction(t)) {
            var sign = sm2.sign(t.getSummary(), nodeName, nodeKeys);
            t.getSign().setVerifierSign(sign);
            return t;
        }
        throw new VerifyFailException(t, nodeName);
    }

    public TransactionTest verifyAndSignWithTime(TransactionTest t) throws VerifyFailException {
        var before = System.currentTimeMillis();

        if (verifyTransaction(t)) {
            var sign = sm2.sign(t.getSummary(), nodeName, nodeKeys);
            t.getSign().setVerifierSign(sign);

            var finish = System.currentTimeMillis();
            verifyTimes.add(finish - before);
            return t;
        }
        throw new VerifyFailException(t, nodeName);
    }

    public List<Long> getVerifyTimes() {
        return verifyTimes;
    }
    

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean verify(Transaction t) throws SignatureException {
        return verifyTransaction((TransactionTest) t);
    }
}
