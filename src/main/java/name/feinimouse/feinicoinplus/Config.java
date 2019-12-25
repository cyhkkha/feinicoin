package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.HashGen;
import name.feinimouse.feinicoinplus.sim.MurmurHashGen;
import name.feinimouse.feinicoinplus.core.SignGen;
import name.feinimouse.feinicoinplus.sim.SM2SignGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:feinicoinplus-config.properties")
public class Config {
    @Value("${coin.hash.seed}")
    private int hashSeed;
    @Value("${coin.hash.long}")
    private boolean hashLong;
    
    // hash机
    @Bean
    public HashGen hashGen() {
        return new MurmurHashGen(hashSeed, hashLong);
    }
    
    // 签名机
    @Bean
    public SignGen signGen() {
        return new SM2SignGen();
    }
    
    // 公钥仓库
//    @Bean
//    public PublicKeyHub publicKeyHub() {
//        return new PublicKeyHub();
//    }
//    
//    // 验证节点，非单例的prototype
//    @Bean @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public Node verifier() {
//        PrivateKey key = signGen().genKeyPair().getPrivate();
//        Verifier node = new Verifier(key);
//        node.setSignGen(signGen());
//        node.setPublicKeyHub(publicKeyHub());
//        return node;
//    }
}
