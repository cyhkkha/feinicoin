package name.feinimouse.feinicoinplus.test;

import name.feinimouse.feinicoinplus.base.BaseConfig;
import name.feinimouse.feinicoinplus.core.SimRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseTestConfig extends BaseConfig {
    
    private final ApplicationContext context;

    @Autowired
    public BaseTestConfig(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @Bean
    public SimRunner simRunner() {
        return new BaseTestRunner(context);
    }
}
