package name.feinimouse.feinicoinplus.deprecated.consensus;

import lombok.Getter;
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

    private ThreadPoolExecutor consensusPool;
    private ThreadPoolExecutor callbackPool;
    private ThreadPoolExecutor confirmPool;

    public ConNodeNet() {
        nodeList = new ArrayList<>();
        consensusPool = new ThreadPoolExecutor(2, Integer.MAX_VALUE
            , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        callbackPool = new ThreadPoolExecutor(3, Integer.MAX_VALUE
            , 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        confirmPool = new ThreadPoolExecutor(3, Integer.MAX_VALUE
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
        consensusPool.execute(() -> randomNodes().forEach(node -> {
            try {
                node.consensus(message);
            } catch (ConsensusException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void callback(ConMessage message) {
        callbackPool.execute(() -> randomNodes().forEach(node -> {
            try {
                node.callback(message);
            } catch (ConsensusException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void confirm(ConMessage message) {
        confirmPool.execute(() -> randomNodes().forEach(node -> {
            try {
                node.confirm(message);
            } catch (ConsensusException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void endRound(ConMessage message) {
        randomNodes().forEach(node -> consensusPool.execute(() -> node.endRound(message)));
    }

    @Override
    public boolean isConfirm() {
        return nodeList.stream().allMatch(ConNode::isConfirm);
    }
}
