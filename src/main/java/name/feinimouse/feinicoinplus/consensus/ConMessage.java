package name.feinimouse.feinicoinplus.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.HashBlockObj;
import name.feinimouse.feinicoinplus.core.data.HashSignObj;
import name.feinimouse.feinicoinplus.core.data.MapSignObj;
import name.feinimouse.feinicoinplus.core.data.Packer;
import org.json.JSONObject;

public class ConMessage extends MapSignObj implements HashBlockObj, HashSignObj {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String hash;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private Packer packer;
    
    @Getter
    @Setter
    private String sender;
    
    public ConMessage(int id) {
        this.id = id;
    }

    public ConMessage clone() {
        return (ConMessage) super.clone();
    }
    
    public ConMessage clone(String type) {
        ConMessage conMessage = clone();
        conMessage.setType(type);
        return conMessage;
    }
    
    public ConMessage clone(String type, String sender) {
        ConMessage conMessage = clone(type);
        conMessage.setSender(sender);
        return conMessage;
    }

    @Override
    public JSONObject genJson() {
        return new JSONObject()
            .put("id", id)
            .put("packer", packer.getHash());
    }
    
    public boolean notSameTask(ConMessage message) {
        return message == null
            || message.id != id
            || !message.hash.equals(hash);
    }
}
