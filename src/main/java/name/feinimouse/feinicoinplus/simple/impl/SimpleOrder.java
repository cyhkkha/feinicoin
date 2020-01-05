package name.feinimouse.feinicoinplus.simple.impl;

import name.feinimouse.feinicoinplus.core.node.Order;
import name.feinimouse.feinicoinplus.exception.NodeRunningException;

@SuppressWarnings("RedundantThrows")
public class SimpleOrder extends Order {
    @Override
    protected void sendBackError() {
        
    }

    @Override
    protected void resolveGapPeriod() throws NodeRunningException {

    }
}
