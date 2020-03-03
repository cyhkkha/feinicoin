package name.feinimouse.feinicoinplus.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.lambda.InputRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractBFTNet implements BFTNet {
    @Getter
    protected String address = ADDRESS_NET;
    
    @Setter
    protected PublicKeyHub publicKeyHub;

    protected ArrayList<AbstractBFTNode> nodeList = new ArrayList<>();
    protected AtomicInteger replyNum = new AtomicInteger(0);

    protected Random random = new Random();
    
    protected BFTNode producer;

    public void putNode(AbstractBFTNode node) {
        if (!nodeList.contains(node)) {
            nodeList.add(node);
            int size = nodeList.size();
            nodeList.forEach(n -> n.setNodeNum(size));
            node.setNet(this);
        }
    }

    @Override
    public void start(BFTMessage bftMessage) {
        int i = random.nextInt(nodeList.size());
        producer = nodeList.get(i);
        producer.start(bftMessage);
    }
    
    protected void broadcast(ThreadPoolExecutor executor, InputRunner<BFTNode> runner) {
        @SuppressWarnings("unchecked") 
        ArrayList<AbstractBFTNode> list = (ArrayList<AbstractBFTNode>) nodeList.clone();
        Collections.shuffle(list);
        list.forEach(node -> executor.execute(() -> runner.run(node)));
    }

    @Override
    public void reply() {
        replyNum.incrementAndGet();
    }

    @Override
    public boolean isConsensus() {
        return replyNum.get() >= nodeList.size();
    }

    @Override
    public void destroy() {
        nodeList.forEach(node -> publicKeyHub.deleteKey(node.getAddress()));
    }
}
