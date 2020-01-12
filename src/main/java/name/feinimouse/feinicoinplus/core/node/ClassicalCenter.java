package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.NetInfo;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.NodeBusyException;
import name.feinimouse.feinicoinplus.core.exception.RequestNotSupportException;
import name.feinimouse.utils.exception.OverFlowException;
import name.feinimouse.utils.exception.UnrecognizedClassException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ClassicalCenter extends CacheNode {
    private Logger logger = LogManager.getLogger(ClassicalCenter.class);
    
    @PropNeeded
    protected VerifierCore verifierCore;
    
    @PropNeeded
    protected CenterCore centerCore;

    // 处理时间，为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置
    protected long verifyTime = 3 * 100;
    // 处理间隔
    @Setter
    protected long verifyInterval = 5;

    public ClassicalCenter(CenterCore centerCore, VerifierCore verifierCore) {
        super(NODE_CENTER_CLASSICAL);
        cacheWait.setMax(50);
        this.centerCore = centerCore;
        this.verifierCore = verifierCore;
    }
    

    @Override
    protected void beforeCommit(Carrier carrier) throws BadRequestException {
        super.beforeCommit(carrier);
        NetInfo netInfo = carrier.getNetInfo();
        if (netInfo.notMatch(NODE_ENTER, MSG_COMMIT_ORDER)) {
            throw new RequestNotSupportException(this, netInfo, "not support");
        }
        Order.checkCommit(carrier);
    }

    @Override
    protected void resolveCommit(Carrier carrier) throws BadRequestException {
        try {
            cacheWait.put(carrier);
        } catch (UnrecognizedClassException e) {
            throw new RequestNotSupportException(this, carrier.getNetInfo()
                , "Class not support: " + carrier.getPacker().objClass());
        } catch (OverFlowException e) {
            logger.warn("Center缓存溢出");
            throw new NodeBusyException(this);
        }
    }
    
}
