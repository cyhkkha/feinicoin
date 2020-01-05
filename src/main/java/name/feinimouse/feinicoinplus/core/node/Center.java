package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.exception.*;
import name.feinimouse.lambda.RunnerStopper;
import name.feinimouse.utils.InputTimer;
import name.feinimouse.utils.ReturnTimer;
import name.feinimouse.utils.StopwatchExecutor;

import java.security.PrivateKey;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Center extends AutoStopNode {
    protected Queue<Carrier> transCache;

    @Getter
    @Setter
    @PropNeeded
    protected CenterContext content;
    @Setter
    @Getter
    @PropNeeded
    protected String ordersAddress;
    @Setter
    @Getter
    @PropNeeded
    protected CenterDao centerDao;
    @Setter
    @Getter
    @PropNeeded
    protected SignGenerator signGen;
    @Setter
    @Getter
    @PropNeeded
    protected HashGenerator hashGen;
    @Setter
    @Getter
    @PropNeeded
    protected PublicKeyHub publicKeyHub;

    @Setter
    @Getter
    @PropNeeded
    protected ConsensusNetwork consensusNetwork;

    protected PrivateKey privateKey;

    // 为了使出块时间控制在1s内，因此需要通过判断上一次网络的同步时间来设置fetch时间
    protected long fetchTime = 3 * 100;
    @Getter
    @Setter
    protected long periodTime = 1000;
    @Getter
    @Setter
    protected long fetchInterval = 10;

    private ReturnTimer<Block> producer = new ReturnTimer<>(this::produceBlock);
    private InputTimer<Block> synchronizer = new InputTimer<>(this::synchronizeBlock);

    public Center(PrivateKey privateKey) {
        super(NODE_CENTER);
        this.privateKey = privateKey;
        transCache = new ConcurrentLinkedQueue<>();
    }

    @Override
    protected void afterWork() {
        transCache.clear();
    }

    @Override
    protected void working() throws NodeRunningException, NodeStopException {

        // 定时拉取并处理
        StopwatchExecutor fetcher = new StopwatchExecutor(fetchInterval, fetchTime, stopper -> {
            transactionLoop(stopper);
            assetTransLoop(stopper);
        });
        fetcher.start();

        // 生产区块

        Block block = producer.start();
        long produceTime = producer.getRunTime();

        // 同步并存储区块

        synchronizer.start(block);
        long syncTime = synchronizer.getRunTime();

        // 如果100ms的时间都不够拉取，则抛出异常
        if (periodTime < syncTime + produceTime + 100) {
            throw new NodeRunningException("sync or produce timeout: "
                + "[sync: " + syncTime + ", produce: " + produceTime + "]");
        }

        fetchTime = periodTime - syncTime + produceTime;

        super.working();
    }

    private Block produceBlock() {
        int transSize = transCache.size();
        if (isStop()) {
            return null;
        }
        Packer[] transArr = new Packer[transSize];
        for (int i = 0; i < transSize; i++) {
            //noinspection ConstantConditions
            transArr[i] = transCache.poll().getPacker();
        }
        PackerArr transTree = hashGen.hash(transArr, Transaction.class);
        PackerArr accountTree = hashGen.hash(content.getAccounts(), Account.class);
        PackerArr assetTree = hashGen.hash(content.getAssets(), Asset.class);

        Packer lastPacker = content.getLastBlock();
        Block lastBlock = (Block) lastPacker.obj();

        Block block = new Block(accountTree, assetTree, transTree);
        block.setId(lastBlock.getId());
        block.setPreHash(lastPacker.getHash());
        block.setProducer(address);
        block.setTimestamp(System.currentTimeMillis());

        resetGap();
        return block;
    }


    private void synchronizeBlock(Block block) {
        if (isStop() || block == null) {
            return;
        }
        resetGap();
        Packer blockPacker = hashGen.hash(block);
        signGen.sign(privateKey, blockPacker, "center");
        try {
            boolean consensusResult = consensusNetwork.commit(blockPacker);
            if (consensusResult) {
                content.commitNewBlock(blockPacker);
                centerDao.saveBlock(blockPacker);
                resetGap();
            }
        } catch (ConsensusException | DaoException e) {
            e.printStackTrace();
        }
    }

    /////////////////
    //   拉   取   //
    /////////////////
    private Carrier fetchValidTrans(Class<?> tClass) throws ControllableException {
        Carrier fetchCarrier = genCarrier(ordersAddress, MSG_FETCH_ORDER, null);
        try {
            Carrier carrier = fetchFromNetWork(fetchCarrier, tClass);
            if (carrier == null) {
                throw new ControllableException("fetch failed");
            }

            Packer packer = carrier.getPacker();
            // 如果fetch的结果不含packer，且packer的类型不符则丢弃
            if (!Optional.ofNullable(packer)
                .map(Packer::objClass)
                .map(c -> c.equals(tClass))
                .orElse(false)) {
                throw new ControllableException("Invalid fetch result");
            }

            AttachInfo attachM = carrier.getAttachInfo();
            // 必须存在各级操作者的信息
            if (packer.getEnter() == null
                || packer.getVerifier() == null
                || packer.getOrder() == null
                || attachM.getVerifiedResult() == null) {
                throw new BadCommitException("Invalid attach message");
            }

            // 必须存在验证者的签名，且必须验证通过
            if (packer.excludeSign(packer.getVerifier())
                || attachM.getVerifiedResult()) {
                throw new ControllableException("Failed verification");
            }
            return carrier;
        } catch (BadCommitException e) {
            e.printStackTrace();
            throw new ControllableException("Fetch format is incorrect");
        }
    }

    private void transactionLoop(RunnerStopper stopper) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(Transaction.class);
            Transaction transaction = (Transaction) carrier.getPacker().obj();
            content.admitTransaction(transaction);
            transCache.add(carrier);
            resetGap();
        } catch (ControllableException | TransAdmitFailedException e) {
            e.printStackTrace();
        }
    }

    private void assetTransLoop(RunnerStopper stopper) {
        if (isStop()) {
            stopper.stop();
            return;
        }
        try {
            Carrier carrier = fetchValidTrans(AssetTrans.class);
            AssetTrans assetTrans = (AssetTrans) carrier.getPacker().obj();
            content.admitAssetTrans(assetTrans);
            resetGap();
        } catch (ControllableException | TransAdmitFailedException e) {
            e.printStackTrace();
        }
    }
    
}
