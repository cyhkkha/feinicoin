package name.feinimouse.feinicoinplus.base.consensus;

import lombok.Getter;
import name.feinimouse.feinicoinplus.consensus.ConMessage;
import name.feinimouse.feinicoinplus.consensus.ConNode;
import name.feinimouse.feinicoinplus.consensus.ConsensusException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component("conNodeNet")
public class ConNodeNet implements ConNode {

    @Getter
    private String address = ADDRESS_NET;

    private ArrayList<ConNode> nodeList;
    
    private ThreadPoolExecutor threadPool;

    public ConNodeNet() {
        nodeList = new ArrayList<>();
        threadPool = new ThreadPoolExecutor(8, Integer.MAX_VALUE
            , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    }

    public void putNode(ConNode conNode) {
        nodeList.add(conNode);
    }

    public int nodeSize() {
        return nodeList.size();
    }

    @SuppressWarnings("unchecked")
    private List<ConNode> randomNodes() {
        ArrayList<ConNode> operateList = (ArrayList<ConNode>) nodeList.clone();
        Collections.shuffle(operateList);
        return operateList;
    }
    
    @Override
    public void consensus(ConMessage message) {
        threadPool.execute(() -> randomNodes().forEach(node -> {
            try {
                node.consensus(message);
            } catch (ConsensusException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void callback(ConMessage message) {
        threadPool.execute(() -> randomNodes().forEach(node -> {
            try {
                node.callback(message);
            } catch (ConsensusException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void confirm(ConMessage message) {
        threadPool.execute(() -> randomNodes().forEach(node -> {
            try {
                node.confirm(message);
            } catch (ConsensusException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void endRound(ConMessage message) {
        threadPool.execute(() -> randomNodes().forEach(node -> node.endRound(message)));
    }

    @Override
    public boolean isConfirm() {
        return nodeList.stream().allMatch(ConNode::isConfirm);
    }
}
