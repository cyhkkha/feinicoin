package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.node.*;
import name.feinimouse.feinicoinplus.core.sim.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.PrivateKey;

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
    public static final int NODE_INTERVAL = 5;
    public static final int FETCH_INTERVAL = 5;
    
    public static final int NUMBER_ACCOUNT = 100;
    public static final int NUMBER_ASSET = 40;
    public static final int NUMBER_ADDRESS = 200;

    @Override
    @Bean
    public AddressManager addressManager() {
        AddressManager addressManager = new SetAddManager();
        addressManager.genAddress(NUMBER_ADDRESS);
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
        manager.genAccount(NUMBER_ACCOUNT);
        return manager;
    }

    @Override
    @Bean
    public AssetManager assetManager() {
        AssetManager manager = new MapAssManager(hashGenerator()
            , addressManager(), accountManager());
        manager.genAsset(NUMBER_ASSET);
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

    private void initNode(BaseNode node) {
        node.setAddress(addressManager().getAddress());
        nodeNetWork().registerNode(node);
        node.setNetwork(nodeNetWork());
        node.setTaskInterval(NODE_INTERVAL);
    }
    
    private PrivateKey genPrivateKey(Node node) {
        KeyPair vKeys = signGenerator().genKeyPair();
        publicKeyHub().setKey(node.getAddress(), vKeys.getPublic());
        return vKeys.getPrivate();
    }
    
    @Override
    @Bean
    public Verifier verifier() {
        Verifier verifier = new BaseVerifier(publicKeyHub(), signGenerator());
        initNode(verifier);
        
        verifier.setPrivateKey(genPrivateKey(verifier));
        return verifier;
    }

    @Override
    @Bean
    public Order order() {
        Order order = new BaseOrder();
        initNode(order);
        
        order.setVerifiersAddress(verifier().getAddress());
        return order;
    }

    @Override
    @Bean
    public Center center() {
        Center center = new BaseCenter(centerContext(), hashGenerator(), consensusNetwork());
        initNode(center);
        
        center.setPrivateKey(genPrivateKey(center));
        center.setOrdersAddress(order().getAddress());
        center.setFetchInterval(FETCH_INTERVAL);
        return center;
    }

    @Override
    @Bean
    public SimRunner simRunner() {
        return null;
    }

}
