package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;

@Configuration
public class BaseConfig implements SimConfig {

    //////////////////////////////////////////////////////////////////
    // 可能会用到的的注解：
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例的bean注解
    // @ComponentScan("name.feinimouse.feinicoinplus.base")
    // @PropertySource("classpath:feinicoinplus-config.properties")
    // @Value("${coin.hash.seed}")
    //////////////////////////////////////////////////////////////////
    
    
    public static final int SEED = 1214;

    @Override
    @Bean
    public AddressManager addressManager() {
        AddressManager addressManager = new SetAddManager();
        addressManager.genAddress(200);
        return addressManager;
    }

    @Override
    @Bean
    public SignGenerator signGenerator() {
        return new SM2SignGen();
    }

    @Override
    @Bean
    public HashGenerator hashGenerator() {
        return new MurmurHashGen(SEED, true);
    }

    @Override
    @Bean
    public CenterDao centerDao() {
        return new SleepCenterDao();
    }

    @Override
    @Bean
    public PublicKeyHub publicKeyHub() {
        return new MapPublicKeyHub();
    }
    
    @Override
    @Bean
    public AccountManager accountManager() {
        AccountManager manager = new ListMapAccMan(hashGenerator()
            , addressManager(), publicKeyHub(), signGenerator());
        manager.genAccount(100);
        return manager;
    }

    @Override
    @Bean
    public AssetManager assetManager() {
        AssetManager manager = new MapAssManager(hashGenerator()
            , addressManager(), accountManager());
        manager.genAsset(40);
        return manager;
    }
    
    @Override
    @Bean
    public CenterContext centerContext() {
        return new BaseCenContext(accountManager(), assetManager(), centerDao());
    }

    @Override
    @Bean
    public ConsensusNetwork consensusNetwork() {
        return new SleepConsensusNet(signGenerator(), hashGenerator());
    }

    @Override
    @Bean
    public TransactionGenerator transactionGenerator() {
        SimpleTransGen generator = new SimpleTransGen(accountManager()
            , assetManager(), hashGenerator(), signGenerator());
        generator.setAddress(addressManager().getAddress());
        return generator;
    }

    @Override
    @Bean
    public NodeNetwork nodeNetWork() {
        return new MapNodeNetwork();
    }

    @Override
    @Bean
    public Verifier verifier() {
        Verifier verifier = new BaseVerifier(publicKeyHub(), signGenerator());
        verifier.setAddress(addressManager().getAddress());
        KeyPair vKeys = signGenerator().genKeyPair();
        verifier.setPrivateKey(vKeys.getPrivate());
        publicKeyHub().setKey(verifier.getAddress(), vKeys.getPublic());
        nodeNetWork().registerNode(verifier);
        verifier.setNetwork(nodeNetWork());
        return verifier;
    }

    @Override
    @Bean
    public Order order() {
        Order order = new BaseOrder();
        order.setAddress(addressManager().getAddress());
        order.setAddress(addressManager().getAddress());
        order.setVerifiersAddress(verifier().getAddress());
        nodeNetWork().registerNode(order);
        order.setNetwork(nodeNetWork());
        return order;
    }

    @Override
    @Bean
    public Center center() {
        Center center = new BaseCenter(centerContext(), hashGenerator(), consensusNetwork());
        center.setAddress(addressManager().getAddress());
        center.setOrdersAddress(order().getAddress());
        KeyPair cKey = signGenerator().genKeyPair();
        center.setPrivateKey(cKey.getPrivate());
        publicKeyHub().setKey(center.getAddress(), cKey.getPublic());
        nodeNetWork().registerNode(center);
        center.setNetwork(nodeNetWork());
        return center;
    }

    @Override
    @Bean
    public SimRunner simRunner() {
        return null;
    }

}
