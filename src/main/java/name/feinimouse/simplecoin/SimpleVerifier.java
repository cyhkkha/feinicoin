package name.feinimouse.simplecoin;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Verifier;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;

public class SimpleVerifier implements Verifier {
    
    private UserManager userManager;
    @Getter @Setter
    private String name;
    @Getter
    private SM2 sm2;
    @Getter
    private List<Long> verifyTimes;
    @Getter
    private List<Long> bundleTimes;

    public SimpleVerifier(@NonNull UserManager userManager) {
        this.sm2 = SM2Generator.getInstance().generateSM2();
        this.userManager = userManager;
        this.verifyTimes = new ArrayList<>();
        this.bundleTimes = new ArrayList<>();
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

    public TransBundle signBundle(TransBundle tb) throws SignatureException {
            var sign = new SimpleSign();
            var before = System.nanoTime();
            sign.setSign("verifier", sm2.signToByte(tb.getHash()));
            this.bundleTimes.add(tb.getBundleTime() + (System.nanoTime() - before)); 
            tb.setSign(sign);
        return tb;
        
    }
    
    public TransBundle verifyBundle(List<Transaction> ts) {
        try {

            var bundle = new TransBundle(ts);
            return signBundle(bundle);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("打包签名交易失败！");
        }
    }
    
}