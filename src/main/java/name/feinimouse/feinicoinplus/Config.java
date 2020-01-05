package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.simple.impl.MurmurHashGen;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.simple.impl.SM2SignGen;
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
    public HashGenerator hashGen() {
        return new MurmurHashGen(hashSeed, hashLong);
    }
    
    // 签名机
    @Bean
    public SignGenerator signGen() {
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
