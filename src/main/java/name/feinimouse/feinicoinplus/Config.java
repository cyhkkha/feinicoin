package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.crypt.HashGen;
import name.feinimouse.feinicoinplus.core.crypt.MurmurHashGen;
import name.feinimouse.feinicoinplus.core.crypt.SignGen;
import name.feinimouse.feinicoinplus.core.crypt.SM2SignGen;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.node.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

import java.security.PrivateKey;

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
    @Bean
    public PublicKeyHub publicKeyHub() {
        return new PublicKeyHub();
    }
    
    // 验证节点，非单例的prototype
    @Bean @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Node verifier() {
        PrivateKey key = signGen().genKeyPair().getPrivate();
        Verifier node = new Verifier(key);
        node.setSignGen(signGen());
        node.setPublicKeyHub(publicKeyHub());
        return node;
    }
}
