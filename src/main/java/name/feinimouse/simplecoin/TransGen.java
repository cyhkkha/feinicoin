package name.feinimouse.simplecoin;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.NonNull;

public class TransGen {
    private Random random;
    private UserManager userManager;
    @Getter
    private List<Long> signTimes;

    public TransGen(@NonNull UserManager userManager) {
        this.userManager = userManager;
        this.random = new Random();
        this.signTimes = new ArrayList<>();
    }

    public SimpleTrans genTransaction() {
        var sender = userManager.getRandomUser();
        var receiver = userManager.getRandomUser(sender);
        var coin = random.nextInt(1000);
        var timestamp = System.currentTimeMillis();
        return new SimpleTrans(timestamp, sender, receiver, coin);
    }

    public SimpleTrans sign(@NonNull SimpleTrans t) throws SignatureException {
        var signer = userManager.getSM2(t.getSender());

        long before = System.nanoTime();
        var signRes = signer.signToByte(t.getSummary());
        long after = System.nanoTime();
        signTimes.add(after - before);

        var signObj = t.getSign();
        if (signObj == null) {
            signObj = new SimpleSign();
        }
        signObj.setSign("sender", signRes);
        t.setSign(signObj);
        return t;
    }

    public SimpleTrans genSignedTrans() {
        try {
            return sign(genTransaction());
        } catch (SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}