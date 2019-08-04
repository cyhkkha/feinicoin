package name.feinimouse.simplecoin.manager;

import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.manager.Verifier;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.simplecoin.account.SimpleSign;
import name.feinimouse.simplecoin.account.TransBundle;
import name.feinimouse.simplecoin.core.UserManager;

public class SimpleVerifier implements Verifier {
    @Getter
    protected UserManager userManager;
    @Getter @Setter
    protected String name;
    @Getter
    protected SM2 sm2;
    
    // 自动统计验证时间
    @Getter
    protected List<Long> verifyTimes;
    // 自动统计打包时间
    @Getter
    protected List<Long> bundleTimes;

    // 获取验证节点的公钥
    public PublicKey getPublicKey() {
        return sm2.getPublicKey();
    }

    /**
     * 构造一个verifier
     * @param userManager 用户管理器
     */
    public SimpleVerifier(@NonNull UserManager userManager) {
        this.sm2 = SM2Generator.getInstance().generateSM2();
        this.userManager = userManager;
        this.verifyTimes = new Vector<>();
        this.bundleTimes = new Vector<>();
    }
    
    // 验证一条交易
    public boolean verify(@NonNull Transaction t) {
        return verify(t, t.getSender());
    }
    
    // 用指定user的公钥验证交易
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

    // 签名一个BCBDC集合
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

    /**
     * 验证一组交易
     * @param ts 交易集合
     * @return 验证通过的交易
     */
    public List<Transaction> verifyList(@NonNull List<Transaction> ts) {
        return ts.stream().filter(this::verify).collect(Collectors.toList());
    }
    
    // 通过交易集合产出一个BCBDC集合
    public TransBundle bundle(@NonNull List<Transaction> ts) {
        var verifiedList = verifyList(ts);
        var bundle = new TransBundle(verifiedList);
        return signBundle(bundle);
    }
    
    // 清除验签时间
    public void clearTime() {
        this.verifyTimes.clear();
    }
    
}