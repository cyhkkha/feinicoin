package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.exception.NodeRunningException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("RedundantThrows")
@Component("order")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleOrder extends Order {
    @Override
    protected void sendBackError() {
        
    }

    @Override
    protected void resolveGapPeriod() throws NodeRunningException {

    }
}
