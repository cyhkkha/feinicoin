package name.feinimouse.feinicoinplus.base.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.NetInfo;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.node.NodeNetwork;
import name.feinimouse.feinicoinplus.core.exception.BadRequestException;
import name.feinimouse.feinicoinplus.core.exception.NoSuchNodeException;
import name.feinimouse.feinicoinplus.core.exception.NodeBusyException;
import name.feinimouse.feinicoinplus.core.exception.NodeNotWorkingException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("nodeNetwork")
public class MapNodeNetwork implements NodeNetwork {
    @Setter
    private long netDelayTime = 0;
    
    @Setter @Getter
    private String address;
    private Map<String, Node> nodeMap;

    public MapNodeNetwork() {
        nodeMap = new ConcurrentHashMap<>();
    }
    
    protected void netDelay() {
        try {
            Thread.sleep(netDelayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String checkCarrier(Carrier carrier) throws NoSuchNodeException {
        String receiver = Optional.ofNullable(carrier)
            .map(Carrier::getNetInfo)
            .map(NetInfo::getReceiver)
            .orElseThrow(() -> new NoSuchNodeException(null));
        if (!nodeMap.containsKey(receiver)) {
            throw new NoSuchNodeException(receiver);
        }
        return receiver;
    }
    
    @Override
    public void commit(Carrier carrier) throws NoSuchNodeException, NodeBusyException {
        String receiver = checkCarrier(carrier);
        try {
            netDelay();
            nodeMap.get(receiver).commit(carrier);
        } catch (NodeBusyException e) {
            throw e;
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Carrier fetch(Carrier carrier) throws NoSuchNodeException {
        String receiver = checkCarrier(carrier);
        try {
            netDelay();
            Carrier result = nodeMap.get(receiver).fetch(carrier);
            netDelay();
            return result;
        } catch (NodeNotWorkingException ignored) {
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void registerNode(Node node) {
        nodeMap.put(node.getAddress(), node);
    }
}
