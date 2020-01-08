package name.feinimouse.feinicoinplus.test;

import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import org.springframework.context.ApplicationContext;


public class BaseTestRunner implements SimRunner {
    
    private final ApplicationContext context;

    public BaseTestRunner(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public ResultManager start(InitParam initParam) {
        Verifier verifier = (Verifier) context.getBean("verifier");
        Order order = (Order) context.getBean("order");
        Center center = (Center) context.getBean("center");
        PublicKeyHub publicKeyHub = (PublicKeyHub) context.getBean("publicKeyHub"); 
        System.out.println(publicKeyHub.addressSet());
        return null;
    }
}
