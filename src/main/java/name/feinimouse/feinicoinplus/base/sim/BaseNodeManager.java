package name.feinimouse.feinicoinplus.base.sim;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.node.ClassicalCenter;
import name.feinimouse.feinicoinplus.core.node.FetchCenter;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.sim.NodeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("nodeManager")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseNodeManager implements NodeManager {
    protected Logger logger = LogManager.getLogger(BaseNodeManager.class);

    @Getter
    protected Verifier verifier;
    @Getter
    protected Order order;
    @Getter
    protected FetchCenter fetchCenter;
    @Getter
    protected ClassicalCenter classicalCenter;

    @Autowired
    public BaseNodeManager(Verifier verifier, Order order
        , FetchCenter fetchCenter, ClassicalCenter classicalCenter) {
        this.verifier = verifier;
        this.order = order;
        this.fetchCenter = fetchCenter;
        this.classicalCenter = classicalCenter;
    }

    @Override
    public void startFetchNode() {
        order.start();
        verifier.start();
        fetchCenter.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("fetch node 启动成功");
    }

    @Override
    public void startClassicalNode() {
        classicalCenter.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("classical node 启动成功");
    }

}
