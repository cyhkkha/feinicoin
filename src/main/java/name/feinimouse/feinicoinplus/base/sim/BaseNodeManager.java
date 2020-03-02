package name.feinimouse.feinicoinplus.base.sim;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.node.*;
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
    
    protected PublicKeyHub publicKeyHub;
    protected NodeNetwork nodeNetwork;

    @Autowired
    public void setVerifier(Verifier verifier) {
        this.verifier = verifier;
    }

    @Autowired
    public void setOrder(Order order) {
        this.order = order;
    }

    @Autowired
    public void setFetchCenter(FetchCenter fetchCenter) {
        this.fetchCenter = fetchCenter;
    }

    @Autowired
    public void setClassicalCenter(ClassicalCenter classicalCenter) {
        this.classicalCenter = classicalCenter;
    }

    @Autowired
    public void setPublicKeyHub(PublicKeyHub publicKeyHub) {
        this.publicKeyHub = publicKeyHub;
    }

    @Autowired
    public void setNodeNetwork(NodeNetwork nodeNetwork) {
        this.nodeNetwork = nodeNetwork;
    }

    @Override
    public void startFetchNode() {
        order.setVerifiersAddress(verifier.getAddress());
        fetchCenter.setOrdersAddress(order.getAddress());
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

    @Override
    public void waitNode() {
        try {
            order.join();
            verifier.join();
            fetchCenter.join();
            classicalCenter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroyNode(AbstractNode node) {
        if (!node.isStop()) {
            node.stopNode();
        }
        nodeNetwork.removeNode(node);
        publicKeyHub.deleteKey(node.getAddress());
    }
    
    @Override
    public void destroy() {
        destroyNode(order);
        destroyNode(verifier);
        destroyNode(fetchCenter);
        destroyNode(classicalCenter);
    }

}
