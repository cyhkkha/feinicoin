package name.feinimouse.feinicoinplus.base;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.core.TransactionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Optional;
import java.util.Random;

@Component("transactionGenerator")
public class SimpleTransGen implements TransactionGenerator {
    private AccountManager accountManager;
    private AssetManager assetManager;
    private HashGenerator hashGenerator;
    private SignGenerator signGenerator;
    
    @Setter
    private String address;
    private Random random = new Random();

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
    @Autowired
    public void setHashGenerator(HashGenerator hashGenerator) {
        this.hashGenerator = hashGenerator;
    }
    @Autowired
    public void setSignGenerator(SignGenerator signGenerator) {
        this.signGenerator = signGenerator;
    }
    @Autowired
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    private AssetTrans genA(String address, String operator, String receiver, int number) {
        if (!(accountManager.contain(operator) && accountManager.contain(receiver))) {
            throw new RuntimeException("can not find account: " + operator + ", " + receiver);
        }
        Asset asset = assetManager.get(address, operator);
        if (asset == null) {
            throw new RuntimeException("no such asset: " + address + "@" + operator);
        }
        return new AssetTrans(address, receiver, operator, number);
    }

    private Packer sign(AssetTrans assetTrans) {
        Packer packer = hashGenerator.hash(assetTrans);
        // 如果有附加普通交易，则普通交易发送者要进行签名
        Optional.ofNullable(assetTrans.getTransaction())
            .ifPresent(transaction -> {
                PrivateKey key = accountManager.getPrivateKey(transaction.getSender());
                signGenerator.sign(key, packer, transaction.getSender());
            });
        // 对资产交易本身进行签名
        PrivateKey key = accountManager.getPrivateKey(assetTrans.getOperator());
        signGenerator.sign(key, packer, assetTrans.getOperator());
        packer.setEnter(this.address);
        return packer;
    }
    
    private Packer sign(Transaction transaction) {
        PrivateKey key = accountManager.getPrivateKey(transaction.getSender());
        Packer packer = hashGenerator.hash(transaction);
        signGenerator.sign(key, packer, transaction.getSender());
        packer.setEnter(this.address);
        return packer;
    }
    
    @Override
    public Packer genRandomTrans() {
        Account sender = accountManager.getRandom();
        Account receiver = accountManager.getRandomEx(sender);
        int coin = random.nextInt(51) + 1;
        return genTrans(sender.getAddress(), receiver.getAddress(), coin);
    }

    @Override
    public Packer genRandomAssetTrans() {
        Asset asset = assetManager.getRandom();
        String sender = asset.getOwner();
        if (random.nextInt(2) > 0) {
            return genAssetTrans(
                asset.getAddress(),
                sender,
                accountManager.getRandomEx(sender).getAddress(),
                random.nextInt(51) + 1,
                random.nextInt(71) + 1
            );
        } else {
            return genAssetTrans(
                asset.getAddress(),
                sender,
                accountManager.getRandomEx(sender).getAddress(),
                random.nextInt(51) + 1
            );
        }
    }

    @Override
    public Carrier genCarrier(Packer packer, String receiver) {
        NetInfo netInfo = new NetInfo(Node.NODE_ENTER, null);
        netInfo.setMsgType(Node.MSG_COMMIT_ORDER);
        netInfo.setSender(this.address);
        netInfo.setReceiver(receiver);
        AttachInfo attachInfo = new AttachInfo();
        Carrier carrier = new Carrier(netInfo, attachInfo);
        carrier.setPacker(packer);
        return carrier;
    }

    @Override
    public Packer genTrans(String sender, String receiver, int coin) {
        if (!(accountManager.contain(sender) && accountManager.contain(receiver))) {
            throw new RuntimeException("can not find account: " + sender + ", " + receiver);
        }
        Transaction transaction = new Transaction(sender, receiver, coin);
        return sign(transaction);
    }
    
    @Override
    public Packer genAssetTrans(String address, String operator, String receiver, int number) {
        AssetTrans assetTrans = genA(address, operator, receiver, number);
        return sign(assetTrans);
    }

    @Override
    public Packer genAssetTrans(String address, String operator, String receiver, int number, Transaction trans) {
        AssetTrans assetTrans = genA(address, operator, receiver, number);
        assetTrans.setTransaction(trans);
        return sign(assetTrans);
    }

    @Override
    public Packer genAssetTrans(String address, String operator, String receiver, int number, int coin) {
        Transaction transaction = new Transaction(receiver, operator, coin);
        return genAssetTrans(address, operator, receiver, number, transaction);
    }
}
