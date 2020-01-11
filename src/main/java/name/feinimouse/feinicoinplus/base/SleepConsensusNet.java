package name.feinimouse.feinicoinplus.base;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.node.ConsensusNetwork;
import name.feinimouse.feinicoinplus.core.node.exception.ConsensusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;

public class SleepConsensusNet implements ConsensusNetwork {
    Logger logger = LogManager.getLogger(SleepConsensusNet.class);

    private SignGenerator signGenerator;
    private HashGenerator hashGenerator;
    @Setter
    private long consensusDelay = 5 * 100;

    public SleepConsensusNet(SignGenerator signGenerator, HashGenerator hashGenerator) {
        this.signGenerator = signGenerator;
        this.hashGenerator = hashGenerator;
    }

    @Override
    public Packer signAndCommit(PrivateKey key, Block block) throws ConsensusException {
        Packer packer = hashGenerator.hash(block);
        signGenerator.sign(key, packer, "center");
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
