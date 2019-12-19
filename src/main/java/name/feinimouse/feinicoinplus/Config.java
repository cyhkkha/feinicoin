package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.crypt.HashGen;
import name.feinimouse.feinicoinplus.core.crypt.MurmurHashGen;
import name.feinimouse.feinicoinplus.core.crypt.SignGen;
import name.feinimouse.feinicoinplus.core.crypt.SM2SignGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:feinicoinplus-config.properties")
public class Config {
    @Value("${coin.hash.seed}")
    private int hashSeed;
    @Value("${coin.hash.long}")
    private boolean hashLong;
    
    @Bean
    public HashGen hashGen() {
        return new MurmurHashGen(hashSeed, hashLong);
    }
    
    @Bean
    public SignGen signGen() throws Exception {
        return new SM2SignGen();
    }
}
