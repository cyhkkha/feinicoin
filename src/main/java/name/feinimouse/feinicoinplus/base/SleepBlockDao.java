package name.feinimouse.feinicoinplus.base;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.data.Block;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.BlockDao;
import name.feinimouse.feinicoinplus.core.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("blockDao")
public class SleepBlockDao implements BlockDao {
    Logger logger = LogManager.getLogger(SleepBlockDao.class);
    
    @Setter
    private long daoDelay = 2 * 100;
    
    @Override
    public Packer findBlock(int id) {
        return null;
    }

    @Override
    public void saveBlock(Packer packer) throws DaoException {
        try {
            Block block = (Block) packer.obj();
            Thread.sleep(daoDelay);
            logger.info("区块(交易{}笔，资产{}笔，账户{}笔)写入成功，编号 {}"
                , block.getTransactions().size()
                , block.getAssets().size()
                , block.getAccounts().size()
                , block.getId()
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new DaoException("dao break");
        }
    }
}
