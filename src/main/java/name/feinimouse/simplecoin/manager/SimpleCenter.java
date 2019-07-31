package name.feinimouse.simplecoin.manager;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Center;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.feinism2.SM2Verifier;
import name.feinimouse.simplecoin.SimpleSign;
import name.feinimouse.simplecoin.TransBundle;
import name.feinimouse.simplecoin.UTXOBundle;
import name.feinimouse.simplecoin.UserManager;
import name.feinimouse.simplecoin.block.*;
import name.feinimouse.simplecoin.mongodao.AccountDao;
import name.feinimouse.simplecoin.mongodao.AssetsDao;
import name.feinimouse.simplecoin.mongodao.MongoDao;
import name.feinimouse.simplecoin.mongodao.TransDao;
import net.openhft.hashing.LongHashFunction;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: SimpleCenter
 * Program : feinicoin
 * Description :
 */
public abstract class SimpleCenter <IN> implements Center {
    protected SimpleOrder<?, IN> order;
    protected UserManager manager;
    private SM2 sm2;

    // 验证节点的公钥验证器
    private SM2Verifier orderVerifier;
    
    // 账户缓存
    private Map<String, Integer> blockAccountMap;
    
    @Getter @Setter
    protected String name;
    
    // 出块时间限制，默认1s
    @Getter @Setter
    protected long outBlockTime = 1000L;
    
    // 数据库存储时间
    @Getter
    protected List<Long> saveTimes;
    
    // 运行开始时间
    private long startTime = 0L;
    // 总运行时间
    @Getter
    private long runTime = 0L;
    // 验证时间
    @Getter
    private long verifyTime = 0L;
    // 是否正在运行
    @Getter
    private boolean running = false;
    
    // 当前区块编号
    private long blockNumber = 0L;
    private String blockPreHash;
    // 出块数量统计
    @Getter
    protected int blockCounts = 0;
    
    public SimpleCenter(@NonNull SimpleOrder<?, IN> order) {
        this.order = order;
        this.manager = order.getUserManager();
        this.blockAccountMap = new ConcurrentHashMap<>();
        this.saveTimes = new LinkedList<>();
        this.sm2 = SM2Generator.getInstance().generateSM2();
        // 初始化verifier节点的验证器
        try {
            var orderKey = order.getPublicKey();
            orderVerifier = new SM2Verifier(orderKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("获取不到Order的公钥");
        }
    }
    
    // 处理交易集
    protected void saveBundle(TransBundle bundle) {
        var bundleSign = bundle.getSign().getByte("verifier");
        var bundleHash = bundle.getHash();
        // 交易集验签
        try {
            orderVerifier.verify(bundleHash, bundleSign);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("验签失败....");
        }
        
        var bundleMap = bundle.getSummary();
        var transList = bundle.getMerkelTree().getList();
        // 将交易列表存入数据库
        var documentList = transList.stream()
            .map(t -> new SimpleHashObj(t).toDocument()).collect(Collectors.toList());
        TransDao.insertList(blockNumber, documentList);
        // 将账户数据并入缓存
        bundleMap.forEach((k, v) -> blockAccountMap.merge(k, v, Integer::sum));
    }
    
    // 处理UTXO
    protected void saveUTXOBundle(UTXOBundle utxoBundle) {
        // 存入交易
        while (!utxoBundle.isEmpty()) {
            var trans = utxoBundle.poll();
            saveTransaction(trans);
        }
        // 存入UTXO记录
        AssetsDao.insertList(blockNumber, new SimpleHashObj(utxoBundle).toDocument());
    }
    
    // 处理单个交易
    protected void saveTransaction(Transaction t) {
        TransDao.insertList(blockNumber, new SimpleHashObj(t).toDocument());
        // 更新账户缓存
        var sender = t.getSender();
        var receiver = t.getReceiver();
        var coin = (Integer)t.getCoin();
        blockAccountMap.merge(sender, - coin, Integer::sum);
        blockAccountMap.merge(receiver, coin, Integer::sum);
    }
    
    // 等待验证
    protected void waitOrRun(IN item, RunCollectTransaction runner) {
        if (item == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("线程意外终止");
            }
        } else  {
            runner.run();
        }
    }
    
    public void stop() {
        if (running) {
            running = false;
        }
        if (startTime > 0L) {
            runTime = System.nanoTime() - startTime;
            startTime = 0L;
        }
    }
    
    // 收集交易的方法
    protected abstract void collectTransaction();
    
    @Override
    public void activate() {
        startTime = System.nanoTime();
        // 多线程执行器
        var executor = Executors.newFixedThreadPool(2);
        // 该线程池中每个线程都维护自己的任务队列。当自己的任务队列执行完成时，会帮助其他线程执行其中的任务。主动找活干
        // Executors.newWorkStealingPool(2);
        // 执行order的验证和排序
        var orderRes = executor.submit(order::activate);
        var centerRes = executor.submit(() -> {
            // 先让order初始化
            Thread.yield();
            while (!order.isFinish()) {
                // 统计出块时间
                var saveTimeStart = System.nanoTime();
                // 出块
                System.out.println("create block...");
                var block = createBlock();
                // 写入数据库
                System.out.println("write block...");
                write(block);
                saveTimes.add(System.nanoTime() - saveTimeStart);
            }
        });
        try {
            // 等待线程运行完毕
            centerRes.get();
            verifyTime = orderRes.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }
    
    // 生成当前区块的账户数据
    private List<SimpleHashObj> createAccountList() {
        var list = new LinkedList<SimpleHashObj>();
        var xxHash = LongHashFunction.xx();
        blockAccountMap.forEach((key, value) -> {
            var json = new JSONObject().put("name", key).put("coin", value).toString();
            var hash = xxHash.hashChars(json);
            list.add(new SimpleHashObj(json, String.valueOf(hash), new SimpleSign()));
        });
        return list;
    }
    
    // 生成区块头
    private SimpleHeader createHeader(String transRoot, String assetsRoot, String accountRoot) {
        var header = new SimpleHeader();
        header.setTimestamp(System.currentTimeMillis());
        header.setProducer("Simple Center");
        header.setTransRoot(transRoot);
        header.setAssetRoot(assetsRoot);
        header.setAccountRoot(accountRoot);
        header.setVersion("0.0.1");
        header.setPreHash(blockPreHash);
        header.setNumber(blockNumber);
        // 生成hash
        var headerJson = header.toJson();
        header.setHash(String.valueOf(LongHashFunction.xx().hashChars(headerJson.toString())));
        // 签名区块
        try {
            var blockSign = new SimpleSign();
            blockSign.setSign("center", sm2.signToByte(headerJson.toString()));
            header.setSign(blockSign);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return header;
    }
    
    @Override
    public Block createBlock() {
        //  初始化新区块
        var preMsg = MongoDao.createNewBlock();
        this.blockNumber = preMsg.getInteger("number");
        this.blockPreHash = preMsg.getString("preHash");
        
        // 从order收集并写入交易
        collectTransaction();
        
        // 生成账户列表
        var blockAccountsList = createAccountList();
        // 从数据库统计交易信息
        var blockTransactionList = TransDao.getList(blockNumber)
            .stream().map(SimpleHashable::new).collect(Collectors.toList());
        // 从数据库统计资产信息
        var blockAssetsList = AssetsDao.getList(blockNumber)
            .stream().map(SimpleHashable::new).collect(Collectors.toList());
        
        // 写入账户信息
        AccountDao.insertList(
            blockNumber,
            blockAccountsList.stream().map(SimpleHashObj::toDocument).collect(Collectors.toList())
        );
        
        // 生成账户、交易、资产三颗默克尔树
        var accounts = new SimpleMerkelTree<>(blockAccountsList);
        accounts.resetRoot();
        var transes = new SimpleMerkelTree<>(blockTransactionList);
        transes.resetRoot();
        var assets = new SimpleMerkelTree<>(blockAssetsList);
        assets.resetRoot();
        
        // 生成区块头
        var header = createHeader(
            transes.getRoot(),
            assets.getRoot(),
            accounts.getRoot()
        );
        
        // 生成区块
        return new SimpleBlock(accounts,assets,transes,header);
    }
    
    @Override
    public void write(Block b) {
        var header = (SimpleHeader)b.getHeader();
        var headerJson = header.toJson();
        headerJson.remove("number");
        headerJson.remove("preHash");
        // 将区块头转化为可写内容
        var headerD = new SimpleHashObj(
            headerJson.toString(),
            header.getHash(),
            header.getSign()
        );
        // 写入交易和资产的头信息
        AccountDao.setRoot(blockNumber, b.getAccounts().getRoot());
        TransDao.setRoot(blockNumber, b.getTransactions().getRoot());
        AssetsDao.setRoot(blockNumber, b.getAssets().getRoot());
        
        // 写入头
        MongoDao.insertHeader(blockNumber, headerD.toDocument());
        blockCounts ++;
    }

    @Override
    public void broadcast() {}

    @Override
    public void syncBlock(Block b) {}

    protected interface RunCollectTransaction{
        void run();
    }
}
