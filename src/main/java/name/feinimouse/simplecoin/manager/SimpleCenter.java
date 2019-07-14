package name.feinimouse.simplecoin.manager;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.manager.Center;
import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;
import name.feinimouse.simplecoin.SimpleSign;
import name.feinimouse.simplecoin.UserManager;
import name.feinimouse.simplecoin.block.*;
import net.openhft.hashing.LongHashFunction;
import org.bouncycastle.util.encoders.Hex;
import org.bson.Document;
import org.json.JSONObject;

import java.security.SignatureException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: SimpleCenter
 * Program : feinicoin
 * Description :
 */
public abstract class SimpleCenter implements Center {
    protected SimpleOrder order;
    protected UserManager manager;
    protected SM2 sm2;
    
    // 账户缓存
    protected Map<String, Integer> blockAccountMap;
    private List<SimpleHashObj> blockAccountsList;
    // 交易缓存
    protected List<SimpleHashObj> bolckTransactionList;
    // 资产缓存
    protected List<SimpleHashObj> blockAssetsList;
    
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
    
    @Getter
    private boolean running = false;
    
    public SimpleCenter(@NonNull SimpleOrder order) {
        this.order = order;
        this.manager = order.getUserManager();
        this.blockAccountMap = new ConcurrentHashMap<>();
        this.bolckTransactionList = new LinkedList<>();
        this.blockAssetsList = new LinkedList<>();
        this.blockAccountsList = new LinkedList<>();
        this.saveTimes = new LinkedList<>();
        this.sm2 = SM2Generator.getInstance().generateSM2();
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
                // 从order收集交易
                collectTransaction();
                // 统计写入时间
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
            centerRes.get();
            verifyTime = orderRes.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    @Override
    public Block createBlock() {
        // 生成当前区块的账户数据
        blockAccountsList.clear();
        var xxHash = LongHashFunction.xx();
        blockAccountMap.forEach((key, value) -> {
            var json = new JSONObject().put("name", key).put("coin", value).toString();
            var hash = xxHash.hashChars(json);
            blockAccountsList.add(new SimpleHashObj(json, String.valueOf(hash), new SimpleSign()));
        });
        
        // 生成默克尔树
        var accounts = new SimpleMerkelTree<>(blockAccountsList);
        accounts.resetRoot();
        var transes = new SimpleMerkelTree<>(bolckTransactionList);
        transes.resetRoot();
        var assets = new SimpleMerkelTree<>(blockAssetsList);
        assets.resetRoot();
        
        // 生成区块头
        var summary = new JSONObject();
        summary.put("number", 0)
            .put("preHash", "0000000000")
            .put("timestamp", System.currentTimeMillis())
            .put("producer", "Simple Center")
            .put("transRoot", transes.getRoot())
            .put("assetRoot", assets.getRoot())
            .put("accountRoot", accounts.getRoot())
            .put("version", "0.0.1");
        var header = new SimpleHeader(summary);
        
        // 签名区块
        try {
            var blockSign = new SimpleSign();
            blockSign.setSign("center", sm2.signToByte(summary.toString()));
            header.setSign(blockSign);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 生成区块
        return new SimpleBlock(accounts,assets,transes,header);
    }
    
    @Override
        public void write(Block b) {
        var block = (SimpleBlock)b;
        // 将默克尔树转化为可写内容
        Document transaction = new Document("list", block.getTransDocs());
        Document account = new Document("list", block.getAccountDocs());
        Document asset = new Document("list", block.getAssetDocs());
        
        var header = block.getHeader();
        // 将区块头转化为可写内容
        var headerD = new Document()
            .append("hash", LongHashFunction.xx().hashChars(header.getSummary()))
            .append("sign", header.getSign().toDoc())
            .append("content", Document.parse(header.getSummary()));
        
        // 写入
        MongoDao. insertTransaction(transaction);
        MongoDao.insertAccount(account);
        MongoDao.insertAssets(asset);
        MongoDao.insertBlock(headerD);
    }

    @Override
    public void broadcast() {}

    @Override
    public void syncBlock(Block b) {}
    
}
