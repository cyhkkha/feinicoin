package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

@Data
public class AdmitPacker {
    private String enter;
    private String order;
    private String verifier;
    private Packer packer;

    public AdmitPacker(Packer packer) {
        this.packer = packer;
    }

    public AdmitPacker(AttachInfo attachInfo, Packer packer) {
        enter = attachInfo.getEnter();
        order = attachInfo.getOrder();
        verifier = attachInfo.getVerifier();
        this.packer = packer;
    }
}
