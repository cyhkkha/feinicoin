package name.feinimouse.feinicoinplus.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;

import java.security.PrivateKey;

public abstract class AbstractBFTNode implements BFTNode {
    
    @Setter
    @Getter
    protected String address;

    @Setter
    protected BFTNet net;
    @Setter
    protected SignGenerator signGenerator;

    @Setter
    protected PublicKeyHub publicKeyHub;

    @Setter
    protected PrivateKey privateKey;

    @Setter
    protected int nodeNum = 1;

    protected BFTMessage prepareMessage;
    
    protected String stage = STAGE_STOP;
    
}
