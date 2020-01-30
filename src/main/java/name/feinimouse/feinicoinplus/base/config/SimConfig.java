package name.feinimouse.feinicoinplus.base.config;

import name.feinimouse.feinicoinplus.base.SimpleTransGen;
import name.feinimouse.feinicoinplus.base.SleepBlockDao;
import name.feinimouse.feinicoinplus.base.SleepConsensusNet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimConfig extends BaseConfig implements InitializingBean {

    @Value("${CONSENSUS_TIME}")
    protected long CONSENSUS_TIME;

    @Value("${BLOCK_SAVE_TIME}")
    protected long BLOCK_SAVE_TIME;

    @Override
    public void afterPropertiesSet() {
        // 初始化交易生成器
        SimpleTransGen simpleTransGen = (SimpleTransGen) transactionGenerator;
        simpleTransGen.setAddress(addressManager.getAddress());

        // 初始化共识层
        SleepConsensusNet consensusNet = (SleepConsensusNet) consensusNetwork;
        consensusNet.setConsensusDelay(CONSENSUS_TIME);
        // 初始化存储层
        SleepBlockDao dao = (SleepBlockDao) blockDao;
        dao.setDaoDelay(BLOCK_SAVE_TIME);
    }
}
