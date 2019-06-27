package name.feinimouse.simplecoin;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

public class TransBundle {
    @Getter @Setter
    private SimpleSign sign;
    private SimpleMerkelTree merkelTree;
    @Getter
    private String transSummary;
    
    private Map<String, Integer> summaryMap;

    public TransBundle() {
        this.summaryMap = new HashMap<String, Integer>();
        this.merkelTree = new SimpleMerkelTree();
    }

    public TransBundle(SimpleTrans[] trans) {
        this();
        addTranses(trans);
    }
    
    public void addTrans(@NonNull SimpleTrans t) {
        var sender = t.getSender();
        var receiver = t.getReceiver();
        var coin = t.getCoinInt();
        var senderCoin = summaryMap.get(sender);
        var receiverCoin = summaryMap.get(receiver);
        if (senderCoin == null) {
            senderCoin = 0;
        }
        if (receiverCoin == null) {
            receiverCoin = 0;
        }
        summaryMap.put(sender, senderCoin - coin);
        summaryMap.put(receiver, receiverCoin + coin);
        merkelTree.addChild(t);
    }

    public void addTranses(SimpleTrans[] ts) {
        for (SimpleTrans t : ts) {
            addTrans(t);
        }
    }

    public String resetSummary() {
        JSONObject json = new JSONObject();
        summaryMap.forEach((key, value) -> json.put(key, value));
        this.transSummary = json.toString();
        return this.transSummary;
    }
}