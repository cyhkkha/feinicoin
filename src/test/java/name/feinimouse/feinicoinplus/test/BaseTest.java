package name.feinimouse.feinicoinplus.test;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.sim.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseTestConfig.class)
public class BaseTest {
    
    @Autowired
    private ApplicationContext context;
    @Autowired
    TransactionGenerator transactionGenerator;
    @Autowired
    AccountManager accountManager;
    @Autowired
    AssetManager assetManager;
    @Autowired
    HashGenerator hashGenerator;
    
    @Test
    public void testTransGen() {
        Packer transactionP = transactionGenerator.genRandomTrans();
        Packer assetTransP = transactionGenerator.genRandomAssetTrans();
        System.out.println(transactionP.genJson());
        System.out.println(assetTransP.genJson());
        
        Transaction transaction = (Transaction) transactionP.obj();
        AssetTrans assetTrans = (AssetTrans) assetTransP.obj();

        Assert.assertTrue(accountManager.contain(transaction.getSender()));
        Assert.assertTrue(accountManager.contain(transaction.getReceiver()));
        
        Account account = accountManager.get(transaction.getSender());
        System.out.println(BlockObj.genJson(account));
        
        Assert.assertTrue(assetManager.contain(assetTrans.getAddress(), assetTrans.getOperator()));
        
        Asset asset = assetManager.get(assetTrans.getAddress(), assetTrans.getOperator());
        System.out.println(BlockObj.genJson(asset));
        
        Packer[] histories = assetManager.getHistories(assetTrans.getAddress(), assetTrans.getOperator());
        Assert.assertEquals(histories.length, 1);
        histories = Arrays.copyOf(histories, 2);
        histories[1] = hashGenerator.hash(assetTrans);
        asset.setHistories(hashGenerator.hash(histories, AssetTrans.class));
        System.out.println(BlockObj.genJson(asset).put("histories", asset.getHistories().genJson()));
    }
    
    @Test
    public void testTransCommit() {
        {
            Packer transP = transactionGenerator.genRandomTrans();
            Transaction transaction = (Transaction) transP.obj();
            Account sender = accountManager.get(transaction.getSender());
            Account receiver = accountManager.get(transaction.getReceiver());
            int coin = transaction.getCoin();
            int coinS = sender.getCoin();
            int coinR = receiver.getCoin();
            accountManager.commit(transaction);
            Assert.assertEquals(coinS, sender.getCoin() + coin);
            Assert.assertEquals(coinR, receiver.getCoin() - coin);
        }
        {
            Packer assetTransP = transactionGenerator.genRandomAssetTrans();
            AssetTrans assetTrans = (AssetTrans) assetTransP.obj();
            String address = assetTrans.getAddress();
            String senderA = assetTrans.getOperator();
            String receiverA = assetTrans.getReceiver();
            Asset sender = assetManager.get(address, senderA);
            int coin = assetTrans.getNumber();
            int coinS = sender.getNumber();
            int coinR = Optional.ofNullable(assetManager.get(address, receiverA))
                .map(Asset::getNumber).orElse(0);
            assetManager.commit(assetTransP);
            Asset receiver = assetManager.get(address, receiverA);

            Assert.assertEquals(coinS, sender.getNumber() + coin);
            Assert.assertEquals(coinR, receiver.getNumber() - coin);

            Packer[] historiesS = assetManager.getHistories(address, senderA);
            Packer[] historiesR = assetManager.getHistories(address, receiverA);
            Assert.assertEquals(historiesS.length, 2);
            Assert.assertEquals(historiesR.length, 2);
            Assert.assertEquals(historiesS[1], historiesR[1]);
        }
    }
    
    public void testVerifier() {
        
    }
    
    @Test
    public void testJava() {
        Random random = new Random();
        int i = 0;
        while (i < 10) {
            System.out.println(random.nextInt(2));
            i++;
        }
    }
}
