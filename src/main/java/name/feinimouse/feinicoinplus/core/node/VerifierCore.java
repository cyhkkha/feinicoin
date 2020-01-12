package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.AssetTrans;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;

// 包含验证节点最基础的功能
public class VerifierCore {
    @Setter
    protected PublicKeyHub publicKeyHub;
    @Setter
    protected SignGenerator signGenerator;

    // 验证资产交易
    public boolean verifyAssetTrans(Packer packer) {
        AssetTrans assetTrans = (AssetTrans) packer.obj();
        boolean result;
        // 先验证资产签名
        {
            String signer = assetTrans.getOperator();
            PublicKey key = publicKeyHub.getKey(signer);
            result = signGenerator.verify(key, packer, signer);
        }
        // 若有附加交易则验证附加交易
        if (assetTrans.getTransaction() != null) {
            String signer = assetTrans.getTransaction().getSender();
            PublicKey key = publicKeyHub.getKey(signer);
            result = result && signGenerator.verify(key, packer, signer);
        }
        return result;
    }

    // 验证普通交易
    public boolean verifyTransaction(Packer packer) {
        Transaction trans = (Transaction) packer.obj();
        String signer = trans.getSender();
        PublicKey key = publicKeyHub.getKey(signer);
        return signGenerator.verify(key, packer, signer);
    }
    
    // 签名一个交易
    public Packer sign(PrivateKey privateKey, Packer packer, String address) {
        return signGenerator.sign(privateKey, packer, address);
    }
}
