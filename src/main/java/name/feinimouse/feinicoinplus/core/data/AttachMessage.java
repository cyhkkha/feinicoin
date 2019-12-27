package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class AttachMessage {
    private String msg;
    private boolean verifiedResult;
    private String verifier;
    private String order;
    private String enter;
    private String password;
}
