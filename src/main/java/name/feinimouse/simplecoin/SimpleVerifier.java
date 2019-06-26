package name.feinimouse.simplecoin;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import name.feinimouse.feinicoin.account.Transaction;

public class SimpleVerifier {
    private UserManager userManager;
    @Getter
    private List<Long> verifyTimes;

    public SimpleVerifier(@NonNull UserManager userManager) {
        this.userManager = userManager;
        this.verifyTimes = new ArrayList<>();
    }

    public boolean verify(@NonNull Transaction t) throws SignatureException {
        var verifier = userManager.getSM2(t.getSender());
        var sign = t.getSign().getByte("sender");
        if (sign == null) {
            throw new NullPointerException("发送者还没有签名");
        }

        long before = System.nanoTime();
        var signRes = verifier.verify(t.getSummary(), sign);
        long after = System.nanoTime();
        verifyTimes.add(after - before);

        return signRes;
    }

    

}