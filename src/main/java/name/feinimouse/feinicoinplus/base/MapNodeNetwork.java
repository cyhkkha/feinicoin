package name.feinimouse.feinicoinplus.base;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import name.feinimouse.feinicoinplus.core.data.NetInfo;
import name.feinimouse.feinicoinplus.core.node.Node;
import name.feinimouse.feinicoinplus.core.node.NodeNetwork;
import name.feinimouse.feinicoinplus.exception.BadCommitException;
import name.feinimouse.feinicoinplus.exception.NoSuchNodeException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MapNodeNetwork implements NodeNetwork {
    @Setter @Getter
    private String address;
    private Map<String, Node> nodeMap;

    public MapNodeNetwork() {
        nodeMap = new ConcurrentHashMap<>();
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
    public void commit(Carrier carrier) throws NoSuchNodeException {
        String receiver = checkCarrier(carrier);
        try {
            nodeMap.get(receiver).commit(carrier);
        } catch (BadCommitException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Carrier fetch(Carrier carrier) throws NoSuchNodeException {
        String receiver = checkCarrier(carrier);
        try {
            return nodeMap.get(receiver).fetch(carrier);
        } catch (BadCommitException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void registerNode(Node node) {
        nodeMap.put(node.getAddress(), node);
    }
}
