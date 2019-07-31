package name.feinimouse.simplecoin.manager;

import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.manager.Verifier;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.simplecoin.SimpleSign;
import name.feinimouse.simplecoin.TransBundle;
import name.feinimouse.simplecoin.UserManager;

public class SimpleVerifier implements Verifier {
    @Getter
    protected UserManager userManager;
    @Getter @Setter
    protected String name;
    @Getter
    protected SM2 sm2;
    @Getter
    protected List<Long> verifyTimes;
    @Getter
    protected List<Long> bundleTimes;

    public PublicKey getPublicKey() {
        return sm2.getPublicKey();
    }
    
    public SimpleVerifier(@NonNull UserManager userManager) {
        this.sm2 = SM2Generator.getInstance().generateSM2();
        this.userManager = userManager;
        this.verifyTimes = new ArrayList<>();
        this.bundleTimes = new ArrayList<>();
    }
    
    public boolean verify(@NonNull Transaction t) {
        return verify(t, t.getSender());
    }
    
    public boolean verify(@NonNull Transaction t, String user){
        var verifier = userManager.getSM2(user);
        var sign = t.getSign().getByte("sender");
        if (sign == null) {
            throw new NullPointerException("发送者还没有签名");
        }
        long before = System.nanoTime();
        var signRes = false;
        try {
            signRes = verifier.verify(t.getSummary(), sign);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        long after = System.nanoTime();
        verifyTimes.add(after - before);

        return signRes;
    }

    public TransBundle signBundle(@NonNull TransBundle tb) {
        var sign = new SimpleSign();
        var hash = tb.getHash();
        if (hash == null) {
            throw new NullPointerException("该包为空或者没有初始化");
        }
        try {
            var before = System.nanoTime();
            sign.setSign("verifier", sm2.signToByte(hash));
            this.bundleTimes.add(tb.getBundleTime() + (System.nanoTime() - before)); 
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
            tb.setSign(sign);
        return tb;
    }
    
    public List<Transaction> verifyList(@NonNull List<Transaction> ts) {
        return ts.stream().filter(this::verify).collect(Collectors.toList());
    }
    
    public TransBundle bundle(@NonNull List<Transaction> ts) {
        var verifiedList = verifyList(ts);
        var bundle = new TransBundle(verifiedList);
        return signBundle(bundle);
    }
    
    public void clearTime() {
        this.verifyTimes.clear();
    }
    
}