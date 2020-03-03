package name.feinimouse.feinicoinplus.base.consensus;

import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component("dpos_bftNet")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DPOS_BFTNet extends PBFTNet {

    protected ThreadPoolExecutor prePreparePool = new ThreadPoolExecutor(2, Integer.MAX_VALUE
        , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    
    @Override
    public void prePrepare(BFTMessage bftMessage) {
        prePreparePool.execute(() -> producer.prePrepare(bftMessage));
    }

    @Override
    public void destroy() {
        prePreparePool.shutdown();
        try {
            prePreparePool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.destroy();
    }
}
