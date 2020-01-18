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
    
    public ConMessage(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public ConMessage(ConMessage message, String type) {
        this(message.id, type);
        this.packer = message.packer;
        this.hash = message.hash;
    }
    
    @Override
    public JSONObject genJson() {
        return new JSONObject()
            .put("id", id)
            .put("packer", packer.getHash());
    }
    
}
