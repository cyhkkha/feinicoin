package name.feinimouse.simplecoin.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.account.MixedBundle;
import name.feinimouse.simplecoin.account.SimpleSign;
import name.feinimouse.simplecoin.account.SimpleTransaction;
import name.feinimouse.simplecoin.account.UTXOBundle;
import name.feinimouse.utils.LoopUtils;
import net.openhft.hashing.LongHashFunction;

import java.security.SignatureException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class TransactionGen {
    private Random random;
    private UserManager userManager;
    private LongHashFunction xxHash;
    @Getter
    private List<Long> signTimes;
    @Getter @Setter
    private int coinLimit = 1000;

    // 初始化
    public TransactionGen(@NonNull UserManager userManager) {
        this.userManager = userManager;
        this.random = new Random();
        this.xxHash = LongHashFunction.xx();
        this.signTimes = new Vector<>();
    }

    // 生成一笔随即交易
    public SimpleTransaction genTransaction() {
        var sender = userManager.getRandomUser();
        var receiver = userManager.getRandomUser(sender);
        return genTransaction(sender, receiver);
    }
    
    // 生成一笔随即金额指定发送人和接收人的交易
    public SimpleTransaction genTransaction(String sender, String receiver) {
        var coin = random.nextInt(coinLimit);
        var timestamp = System.currentTimeMillis();
        var trans = new SimpleTransaction(timestamp, sender, receiver, coin);
        trans.setHash(String.valueOf(xxHash.hashChars(trans.getSummary())));
        return trans;
    }

    // 签名一笔交易
    public SimpleTransaction sign(SimpleTransaction t) throws SignatureException {
        var user = t.getSender();
        return sign(t, user);
    }
    
    // 用指定人的密钥签名一笔交易
    public SimpleTransaction sign(@NonNull SimpleTransaction t, String user) throws SignatureException {
        var signer = userManager.getSM2(user);
        var before = System.nanoTime();
        var signRes = signer.signToByte(t.getSummary());
        signTimes.add(System.nanoTime() - before);

        var signObj = t.getSign();
        if (signObj == null) {
            signObj = new SimpleSign();
        }
        signObj.setSign("sender", signRes);
        t.setSign(signObj);
        return t;
    }

    // 生成一笔签名后的随机交易
    public SimpleTransaction genSignedTrans() {
        try {
            return sign(genTransaction());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("签名错误，交易生成失败！");
        }
    }
    
    public Transaction genSignedTransFa() {
        return genSignedTrans();
    }
    
    // 生成一笔UTXO交易集合
    public UTXOBundle genUTXOBundle(int size) {
        var mode = random.nextInt(2);
        var bundle = new UTXOBundle();
        var user = userManager.getRandomUser();
        LoopUtils.loop(size, () -> {
            var add1 = userManager.getRandomAddress(user);
            var add2 = userManager.getRandomAddress(userManager.getRandomUser(user));
            try {
                if (mode > 0) {
                    var trans = genTransaction(add1, add2);
                    bundle.add(sign(trans, user));
                } else {
                    var trans = genTransaction(add2, add1);
                    bundle.add(sign(trans, user));
                }
            } catch (SignatureException e) {
                e.printStackTrace();
                throw new RuntimeException("生成UTXO交易出错");
            }
        });
        bundle.setOwner(user);
        bundle.setIssuer("SimpleCenter");
        bundle.setType("Test");
        bundle.setTimestamp(System.currentTimeMillis());
        return bundle;
    }
    
    // 生成BCBDC对象
    public MixedBundle genMixedBundle(int utxoSize) {
        return new MixedBundle(genUTXOBundle(utxoSize));
    }
    public MixedBundle genMixedBundle() {
        return new MixedBundle(genSignedTrans());
    }
    
}