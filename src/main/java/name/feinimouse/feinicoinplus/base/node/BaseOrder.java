package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.node.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseOrder extends Order {
    private Logger logger = LogManager.getLogger(BaseOrder.class);

    @Override
    protected void afterWork() {
        if (fetchWait.size() > 0) {
            logger.warn("Order还剩下 {} 笔交易没有被Center处理", fetchWait.size());
        }
        super.afterWork();
    }
}
