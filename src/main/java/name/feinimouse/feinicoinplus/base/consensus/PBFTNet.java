package name.feinimouse.feinicoinplus.base.consensus;

import lombok.Getter;
import name.feinimouse.feinicoinplus.consensus.BFTConNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component("pbftNet")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PBFTNet implements BFTConNode {
    @Getter
    private String address = ADDRESS_NET;
    
    private ArrayList<PBFTConNode> nodeList = new ArrayList<>();
    private AtomicInteger replyNum = new AtomicInteger(0);
    
    private Random random = new Random();
    
    public void putNode(PBFTConNode pbftConNode) {
        if (!nodeList.contains(pbftConNode)) {
            nodeList.add(pbftConNode);
            int size = nodeList.size();
            nodeList.forEach(node -> node.setNodeNum(size));
            pbftConNode.setNet(this);
        }
    }
    
    private ThreadPoolExecutor preparePool = new ThreadPoolExecutor(3, Integer.MAX_VALUE
        , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    private ThreadPoolExecutor commitPool = new ThreadPoolExecutor(3, Integer.MAX_VALUE
        , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    
    public ArrayList<BFTConNode> broadcastList() {
        @SuppressWarnings("unchecked")
        ArrayList<BFTConNode> list = (ArrayList<BFTConNode>)nodeList.clone();
        Collections.shuffle(list);
        return list;
    }
    
    @Override
    public void start(BFTMessage bftMessage) {
        int i = random.nextInt(nodeList.size());
        nodeList.get(i).start(bftMessage);
    }

    @Override
    public void prepare(BFTMessage bftMessage) {
        preparePool.execute(() -> broadcastList()
            .forEach(node -> node.prepare(bftMessage)));
    }

    @Override
    public void commit(BFTMessage bftMessage) {
        commitPool.execute(() -> broadcastList()
            .forEach(node -> node.commit(bftMessage)));
    }

    @Override
    public void reply() {
        replyNum.incrementAndGet();
    }
    
    public boolean isConsensus() {
        return replyNum.get() >= nodeList.size();
    }
    
    public void destroy() {
        preparePool.shutdown();
        commitPool.shutdown();
    }

}
