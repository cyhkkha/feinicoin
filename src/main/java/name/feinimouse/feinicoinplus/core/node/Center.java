package name.feinimouse.feinicoinplus.core.node;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.node.exception.ConsensusException;
import name.feinimouse.feinicoinplus.core.node.exception.DaoException;
import name.feinimouse.feinicoinplus.core.node.exception.TransAdmitFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Center extends AutoStopNode {
    private Logger logger = LogManager.getLogger(Center.class);

    @Setter
    @PropNeeded
    protected CenterContext centerContext;
    
    @Setter
    @PropNeeded
    protected HashGenerator hashGen;
    
    @Setter
    @PropNeeded
    protected ConsensusNetwork consensusNetwork;

    @Setter
    @PropNeeded
    protected PrivateKey privateKey;

    // 生产周期
    @Setter
    protected long periodTime = 1000;

    // 缓存待生产的普通交易
    protected Queue<Carrier> transactionCache;

    public Center(String nodeType) {
        super(nodeType);
        transactionCache = new ConcurrentLinkedQueue<>();
    }

    @Override
    protected void afterWork() {
        super.afterWork();
        transactionCache.clear();
    }

    // 生产区块
    protected Block produceBlock() {
        if (isStop()) {
            return null;
        }
        Packer[] transArr = transactionCache.stream().map(Carrier::getPacker).toArray(Packer[]::new);
        transactionCache.clear();
        return centerContext.pack(hashGen.hash(transArr, Transaction.class), address);
    }

    // 同步区块
    protected void synchronizeBlock(Block block) {
        if (isStop() || block == null) {
            return;
        }
        try {
            // 同步完，获取联合签名后的区块
            Packer consensusResult = consensusNetwork.signAndCommit(privateKey, block);
            // 若同步成功则提交写入
            if (consensusResult != null) {
                centerContext.admit(consensusResult);
            }
        } catch (ConsensusException | DaoException e) {
            e.printStackTrace();
        }
    }

    // 处理交易
    protected void handleTransaction(Carrier carrier) {
        Transaction transaction = (Transaction) carrier.getPacker().obj();
        try {
            centerContext.commit(transaction);
        } catch (TransAdmitFailedException e) {
            logger.warn(e.getMessage());
        }
        transactionCache.add(carrier);
        logger.trace("已处理普通交易({})", transaction);
    }

    // 处理资产
    protected void handleAssetTrans(Carrier carrier) {
        AssetTrans assetTrans = (AssetTrans) carrier.getPacker().obj();
        try {
            centerContext.commit(carrier.getPacker());
        } catch (TransAdmitFailedException e) {
            logger.warn(e.getMessage());
        }
        logger.trace("已处理资产交易({})", assetTrans);
    }

}
