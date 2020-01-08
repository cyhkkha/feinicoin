package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import name.feinimouse.feinicoinplus.core.sim.TransactionGenerator;
import name.feinimouse.feinicoinplus.test.BaseTestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
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
        histories = Arrays.copyOf(histories, histories.length + 1);
        histories[histories.length - 1] = hashGenerator.hash(assetTrans);
        asset.setHistories(hashGenerator.hash(histories, AssetTrans.class));
        System.out.println(BlockObj.genJson(asset).put("histories", asset.getHistories().genJson()));
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
