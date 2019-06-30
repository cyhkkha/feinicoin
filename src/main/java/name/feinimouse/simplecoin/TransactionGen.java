package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.NonNull;
import net.openhft.hashing.LongHashFunction;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionGen {
    private Random random;
    private UserManager userManager;
    private LongHashFunction xxHash;
    @Getter
    private List<Long> signTimes;

    public TransactionGen(@NonNull UserManager userManager) {
        this.userManager = userManager;
        this.random = new Random();
        this.xxHash = LongHashFunction.xx();
        this.signTimes = new ArrayList<>();
    }

    public SimpleTransaction genTransaction() {
        var sender = userManager.getRandomUser();
        var receiver = userManager.getRandomUser(sender);
        var coin = random.nextInt(1000);
        var timestamp = System.currentTimeMillis();
        var trans = new SimpleTransaction(timestamp, sender, receiver, coin);
        trans.setHash(String.valueOf(xxHash.hashChars(trans.getSummary())));
        return trans;
    }

    public SimpleTransaction sign(@NonNull SimpleTransaction t) throws SignatureException {
        var signer = userManager.getSM2(t.getSender());

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

    public SimpleTransaction genSignedTrans() {
        try {
            return sign(genTransaction());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("签名错误，交易生成失败！");
        }
    }
    
}