package name.feinimouse.feinicoinplus.base.node;

import name.feinimouse.feinicoinplus.core.BlockDao;
import name.feinimouse.feinicoinplus.core.ConsensusNetwork;
import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.CenterCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("centerCore")
public class BaseCenterCore extends CenterCore {
    @Autowired
    @Override
    public void setBlockDao(BlockDao blockDao) {
        super.setBlockDao(blockDao);
    }

    @Autowired
    @Override
    public void setCenterContext(CenterContext centerContext) {
        super.setCenterContext(centerContext);
    }

    @Autowired
    @Override
    public void setConsensusNetwork(ConsensusNetwork consensusNetwork) {
        super.setConsensusNetwork(consensusNetwork);
    }

    @Autowired
    @Override
    public void setHashGenerator(HashGenerator hashGenerator) {
        super.setHashGenerator(hashGenerator);
    }

    @Autowired
    @Override
    public void setSignGenerator(SignGenerator signGenerator) {
        super.setSignGenerator(signGenerator);
    }
    
}
