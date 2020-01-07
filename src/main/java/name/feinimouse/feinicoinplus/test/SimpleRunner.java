package name.feinimouse.feinicoinplus.test;

import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.node.Center;
import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.core.node.Verifier;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import name.feinimouse.feinicoinplus.core.SimRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;


@Controller("runner")
public class SimpleRunner implements SimRunner {
    
    private final ApplicationContext context;
    private final AddressManager addressManager;
    private final PublicKeyHub publicKeyHub;
    private final SignGenerator signGenerator;

    @Autowired
    public SimpleRunner(ApplicationContext context, AddressManager addressManager, PublicKeyHub publicKeyHub, SignGenerator signGenerator) {
        this.context = context;
        this.addressManager = addressManager;
        this.publicKeyHub = publicKeyHub;
        this.signGenerator = signGenerator;
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
