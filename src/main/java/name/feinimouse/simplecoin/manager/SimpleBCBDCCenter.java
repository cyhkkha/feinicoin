package name.feinimouse.simplecoin.manager;

import lombok.NonNull;
import name.feinimouse.feinism2.SM2Verifier;
import name.feinimouse.simplecoin.TransBundle;
import name.feinimouse.simplecoin.block.MongoDao;
import name.feinimouse.simplecoin.block.SimpleHashObj;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.stream.Collectors;

public class SimpleBCBDCCenter extends SimpleCenter<TransBundle> {
    private SM2Verifier orderVerifier; 
    
    private boolean verifyBundle(TransBundle tb) {
        var bundleSign = tb.getSign().getByte("verifier");
        var bundleHash = tb.getHash();
        try {
            return orderVerifier.verify(bundleHash, bundleSign);
        } catch (SignatureException e) {
            e.printStackTrace();
            System.out.println("验签失败....");
            return false;
        }
    }
    
    public SimpleBCBDCCenter(@NonNull SimpleBCBDCOrder order) {
        super(order);
        var orderKey = order.getPublicKey();
        try {
            orderVerifier = new SM2Verifier(orderKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("获取不到Order的公钥");
        }
    }

    @Override
    protected void collectTransaction() {
        // 统计出块时间
        var blockRunTime = System.currentTimeMillis();
        var blockNowTime = blockRunTime;
        
        do {
            var bundle = order.pull();
            if (bundle == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException("线程意外终止");
                }
            } else  {
                if (verifyBundle(bundle)) {
                    var bundleMap = bundle.getSummary();
                    var transList = bundle.getMerkelTree().getList();
                    // 将交易列表存入数据库
                    var documentList = transList.stream()
                        .map(t -> new SimpleHashObj(t).toDocument()).collect(Collectors.toList());
                    MongoDao.insertTrans(super.blockNumber, documentList);
                    // 将账户数据并入缓存
                    bundleMap.forEach((k, v) -> super.blockAccountMap.merge(k, v, Integer::sum));
                } else {
                    throw new RuntimeException("bundle验证失败");
                }
            }
            // 更新下一轮的时间
            blockNowTime = System.currentTimeMillis();
        } while (blockNowTime - blockRunTime <= outBlockTime);
        System.out.println("collect time out...");
    }
    
}
