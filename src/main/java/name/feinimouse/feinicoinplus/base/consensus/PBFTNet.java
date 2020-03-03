package name.feinimouse.feinicoinplus.base.consensus;

import name.feinimouse.feinicoinplus.consensus.AbstractBFTNet;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component("pbftNet")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PBFTNet extends AbstractBFTNet {
    
    protected ThreadPoolExecutor preparePool = new ThreadPoolExecutor(2, Integer.MAX_VALUE
        , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    protected ThreadPoolExecutor commitPool = new ThreadPoolExecutor(2, Integer.MAX_VALUE
        , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    

    @Override
    public void prePrepare(BFTMessage bftMessage) {
        
    }

    @Override
    public void prepare(BFTMessage bftMessage) {
        broadcast(preparePool, node -> node.prepare(bftMessage));
    }

    @Override
    public void commit(BFTMessage bftMessage) {
        broadcast(commitPool, node -> node.commit(bftMessage));
    }
    
    @Override
    public void destroy() {
        super.destroy();
        preparePool.shutdown();
        commitPool.shutdown();
    }

}
