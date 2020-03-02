package name.feinimouse.feinicoinplus.consensus;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class BFTMessage implements Cloneable {

    private String message;
    private String sign;
    private String signer;
    private ConcurrentHashMap<String, String> attachSignMap;

    @Override
    public BFTMessage clone() {
        try {
            BFTMessage bftMessage = (BFTMessage) super.clone();
            if (attachSignMap != null) {
                bftMessage.setAttachSignMap(new ConcurrentHashMap<>(attachSignMap));
            }
            return bftMessage;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
