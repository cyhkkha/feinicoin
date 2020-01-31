package name.feinimouse.feinicoinplus.consensus;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.data.HashObj;

@Data
public class ConfirmTag implements Cloneable, HashObj {
    private String[] signerArr;
    private String hash;
    private String sign;

    public ConfirmTag(String[] signerArr) {
        this.signerArr = signerArr;
    }

    @Override
    public ConfirmTag clone() {
        try {
            ConfirmTag confirmTag = (ConfirmTag) super.clone();
            confirmTag.signerArr = signerArr.clone();
            return confirmTag;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
