package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.JsonAble;

import java.util.HashMap;

@Data
public class AdmitPacker implements JsonAble {
    private String enter;
    private String order;
    private String verifier;
    private Packer packer;

    public AdmitPacker() {
    }

    public AdmitPacker(AttachInfo attachInfo, Packer packer) {
        enter = attachInfo.getEnter();
        order = attachInfo.getOrder();
        verifier = attachInfo.getVerifier();
        this.packer = packer;
    }
}
