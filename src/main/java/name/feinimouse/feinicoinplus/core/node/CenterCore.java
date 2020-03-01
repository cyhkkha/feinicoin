package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.BlockDao;
import name.feinimouse.feinicoinplus.core.ConsensusNetwork;
import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.consensus.ConsensusException;
import name.feinimouse.feinicoinplus.core.exception.DaoException;
import name.feinimouse.feinicoinplus.core.exception.TransAdmitFailedException;
import name.feinimouse.utils.TimerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CenterCore {
    private Logger logger = LogManager.getLogger(CenterCore.class);

    @Setter
    protected CenterContext centerContext;

    @Setter
    protected HashGenerator hashGenerator;

    @Setter
    protected SignGenerator signGenerator;

    @Setter
    protected ConsensusNetwork consensusNetwork;

    @Setter
    protected BlockDao blockDao;

    // 缓存待生产的普通交易
    protected Queue<Carrier> transactionCache;

    public CenterCore() {
        transactionCache = new ConcurrentLinkedQueue<>();
    }

    // 处理交易
    public void handleTransaction(Carrier carrier) {
        Transaction transaction = (Transaction) carrier.getPacker().obj();
        try {
            centerContext.commitTrans(transaction);
        } catch (TransAdmitFailedException e) {
            logger.warn(e.getMessage());
        }
        transactionCache.add(carrier);
        logger.trace("已处理普通交易({})", transaction);
    }

    // 处理资产
    public void handleAssetTrans(Carrier carrier) {
        AssetTrans assetTrans = (AssetTrans) carrier.getPacker().obj();
        try {
            centerContext.commitAssetTrans(carrier.getPacker());
        } catch (TransAdmitFailedException e) {
            logger.warn(e.getMessage());
        }
        logger.trace("已处理资产交易({})", assetTrans);
    }

    // 执行整个生产流程
    public long executeProduce(String address, PrivateKey key) {
        // 打包区块
        var produceResult = TimerUtils.run(this::packBlock, address);
        Block block = produceResult.get();
        long packTime = produceResult.getTime();

        // 签名区块
        Packer packer = hashGenerator.hash(block);
        signGenerator.sign(key, packer, address);
        packer.setCenter(address);

        // 同步区块
        var syncResult = TimerUtils.run(this::synchronizeBlock, packer);
        long syncTime = syncResult.getTime();

        // 存储区块
        long saveTime = TimerUtils.run(this::saveBlock, syncResult.get());

        long consumeTime = packTime + syncTime + saveTime;

        logger.trace("打包耗时 {}ms ，同步耗时 {}ms ，总共耗时 {}ms ，存储耗时 {}ms"
            , packTime, syncTime, consumeTime, saveTime);
        
        return consumeTime;
    }

    // 生产区块
    public Block packBlock(String address) {
        Packer[] transArr = transactionCache.stream().map(Carrier::getPacker).toArray(Packer[]::new);
        transactionCache.clear();
        return centerContext.pack(hashGenerator.hash(transArr, Transaction.class), address);
    }

    // 同步区块
    public Packer synchronizeBlock(Packer packer) {
        return Optional.ofNullable(packer)
            .map(p -> {
                try {
                    return consensusNetwork.commit(p);
                } catch (ConsensusException e) {
                    e.printStackTrace();
                }
                return null;
            }).orElse(null);
    }

    // 存储区块
    public void saveBlock(Packer packer) {
        Optional.ofNullable(packer)
            .ifPresent(p -> {
                try {
                    blockDao.saveBlock(p);
                    centerContext.admit(p);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            });
    }

    public void close() {
        transactionCache.clear();
    }

}
