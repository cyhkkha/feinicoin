package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.node.VerifierCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 包含验证节点最基础的功能
@Component("verifierCore")
public class BaseVerifierCore extends VerifierCore {
    
    @Autowired
    @Override
    public void setSignGenerator(SignGenerator signGenerator) {
        super.setSignGenerator(signGenerator);
    }

    @Autowired
    @Override
    public void setPublicKeyHub(PublicKeyHub publicKeyHub) {
        super.setPublicKeyHub(publicKeyHub);
    }
}
