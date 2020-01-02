package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class AttachInfo implements Cloneable {
    private String msg;
    private Boolean verifiedResult;
    private String verifier;
    private String order;
    private String enter;
    private String password;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AttachInfo copy() {
        try {
            return (AttachInfo) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
