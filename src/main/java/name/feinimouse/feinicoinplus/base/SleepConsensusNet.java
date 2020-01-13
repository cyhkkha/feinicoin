package name.feinimouse.feinicoinplus.base;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.ConsensusNetwork;
import name.feinimouse.feinicoinplus.consensus.ConsensusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("consensusNetwork")
public class SleepConsensusNet implements ConsensusNetwork {
    Logger logger = LogManager.getLogger(SleepConsensusNet.class);
    
    @Setter
    private long consensusDelay = 5 * 100;

    @Override
    public Packer commit(Packer packer) throws ConsensusException {
        try {
            Thread.sleep(consensusDelay);
            logger.trace("区块共识成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ConsensusException("consensus break");
        }
        return packer;
    }
}
