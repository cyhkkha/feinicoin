package name.feinimouse.feinicoinplus.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.HashBlockObj;
import name.feinimouse.feinicoinplus.core.data.HashSignObj;
import name.feinimouse.feinicoinplus.core.data.MapSignObj;
import org.json.JSONObject;

import java.util.Optional;

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
    private ConfirmTag confirmTag;
    
    @Getter
    @Setter
    private String sender;
    
    public ConMessage(int id) {
        this.id = id;
    }

    public ConMessage clone() {
        ConMessage conMessage = (ConMessage) super.clone();
        Optional.ofNullable(confirmTag).ifPresent(c -> conMessage.setConfirmTag(c.clone()));
        return conMessage;
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
            .put("packer", confirmTag.getHash());
    }
    
    public boolean notSameTask(ConMessage message) {
        return message == null
            || message.id != id
            || !message.hash.equals(hash);
    }
}
