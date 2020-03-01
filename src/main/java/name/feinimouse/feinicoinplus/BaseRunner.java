package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.base.sim.BaseNodeManager;
import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.sim.NodeManager;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("simRunner")
@ComponentScan("name.feinimouse.feinicoinplus.base")
@PropertySource("classpath:feinicoinplus-config-test.properties")
public class BaseRunner implements SimRunner {
    //////////////////////////////////////////////////////////////////
    // 可能会用到的的注解：
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 非单例的bean注解
    // @ComponentScan("name.feinimouse.feinicoinplus.base")
    // @PropertySource("classpath:feinicoinplus-config-test.properties")
    // @Value("${coin.hash.seed}")
    //////////////////////////////////////////////////////////////////

    protected Logger logger = LogManager.getLogger(BaseRunner.class);
    
    protected NodeManager nodeManager;

    @Autowired
    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    @Override
    public ResultManager start(InitParam initParam) {
        BaseNodeManager baseNodeManager = (BaseNodeManager) nodeManager;
        baseNodeManager.startClassicalNode();
        logger.info("节点已启动");

        long time = TimerUtils.run(() -> {
            baseNodeManager.sendRandomMixTransClassical(100, 0);
            try {
                baseNodeManager.getClassicalCenter().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("总计运行时间 {} ms", time);
        return null;
    }
    

    

}
