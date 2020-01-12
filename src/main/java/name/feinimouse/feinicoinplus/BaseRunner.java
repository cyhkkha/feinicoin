package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("name.feinimouse.feinicoinplus.base")
@PropertySource("classpath:feinicoinplus-config-test.properties")
public class BaseRunner implements SimRunner {
    @Override
    public ResultManager start(InitParam initParam) {
        return null;
    }
    //////////////////////////////////////////////////////////////////
    // 可能会用到的的注解：
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例的bean注解
    // @ComponentScan("name.feinimouse.feinicoinplus.base")
    // @PropertySource("classpath:feinicoinplus-config-test.properties")
    // @Value("${coin.hash.seed}")
    //////////////////////////////////////////////////////////////////

}
