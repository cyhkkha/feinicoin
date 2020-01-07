package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.core.sim.TransactionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Random;

@Component("TransactionGenerator")
public class SimpleTransGen implements TransactionGenerator {
    private final AccountManager accountManager;
    private final AssetManager assetManager;
    private final HashGenerator hashGenerator;
    private final SignGenerator signGenerator;
    
    private String address;
    private Random random = new Random();

    @Autowired
    public SimpleTransGen(AccountManager accountManager
        , AssetManager assetManager, HashGenerator hashGenerator
        , SignGenerator signGenerator
        , AddressManager addressManager) {
        this.accountManager = accountManager;
        this.assetManager = assetManager;
        this.hashGenerator = hashGenerator;
        this.signGenerator = signGenerator;
        address = addressManager.getAddress();
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
        PrivateKey key = accountManager.getPrivateKey(assetTrans.getOperator());
        Packer packer = hashGenerator.hash(assetTrans);
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
        Transaction transaction = new Transaction(operator, receiver, coin);
        return genAssetTrans(address, operator, receiver, number, transaction);
    }
}
