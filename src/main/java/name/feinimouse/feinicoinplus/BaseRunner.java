package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.base.sim.ExperimentManager;
import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.data.SimResult;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("simRunner")
@ComponentScan("name.feinimouse.feinicoinplus.base")
@PropertySource("classpath:feinicoinplus-config-base.properties")
public class BaseRunner implements SimRunner {
    //////////////////////////////////////////////////////////////////
    // 可能会用到的的注解：
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例的bean注解
    // @ComponentScan("name.feinimouse.feinicoinplus.base")
    // @PropertySource("classpath:feinicoinplus-config-base.properties")
    // @Value("${coin.hash.seed}")
    //////////////////////////////////////////////////////////////////

    protected Logger logger = LogManager.getLogger(BaseRunner.class);
    
    @Autowired
    private ExperimentManager experimentManager;

    @Override
    public ResultManager start(InitParam initParam) {
        logger.info("----------这里是预实验--------");
        experimentManager.preRunBlockchainNode();
        logger.info("----------预实验结束----------");
        
//        experimentManager.sendRandomMixTransClassical(0);
//        experimentManager.sendRandomMixTransClassical(1);
//        experimentManager.sendRandomMixTransClassical(0.2);
//        experimentManager.sendRandomMixTransFetch(0.2);
//        experimentManager.sendRandomMixTransClassical(0.5);
//        experimentManager.sendRandomMixTransFetch(0.5);

        return null;
    }

}
