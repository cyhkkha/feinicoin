package name.feinimouse.simplecoin.block;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.NonNull;
import org.bson.Document;
import org.json.JSONObject;

import java.util.*;


/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: MongoDao
 * Program : feinicoin
 * Description :
 */
public class MongoDao {
    private static final String USERNAME = "root";
    private static final String DATABASE = "test";
    private static final char[] PASSWORD = "12345".toCharArray();
    private static final int MaxConnect = 30;
    private static final int MaxWaitThread = 30;
    private static final int MaxTimeOut = 60;
    private static final int MaxWaitTime = 60;
    
    
    private static MongoDatabase db;
    private static MongoCollection<Document> transaction;
    private static MongoCollection<Document> account;
    private static MongoCollection<Document> assets;
    private static MongoCollection<Document> block;
    
    static {
        // var credential = MongoCredential.createCredential(USERNAME, DATABASE, PASSWORD);
        var address = new ServerAddress("localhost", 27017);
        var builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(MaxConnect)
            .threadsAllowedToBlockForConnectionMultiplier(MaxWaitThread)
            .connectTimeout(MaxTimeOut * 1000)
            .maxWaitTime(MaxWaitTime *1000);
        var option = builder.build();
        db = new MongoClient(address, option).getDatabase("simplecoin");
        System.out.println("-------connect mongo success-------");
        transaction = db.getCollection("transaction");
        account = db.getCollection("account");
        assets = db.getCollection("assets");
        block = db.getCollection("block");
    }
    @NonNull
    public static void insertTransaction(JSONObject json) {
        insertTransaction(json.toString());
    }
    @NonNull
    public static void insertTransaction(String json) {
        insertTransaction(Document.parse(json));
    }
    @NonNull
    public static void insertTransaction(Document document) {
        transaction.insertOne(document);
    }
    public static void dropTransaction() {
        transaction.drop();
    }
    
    @NonNull
    public static void insertAccount(JSONObject json) {
        insertAccount(json.toString());
    }
    @NonNull
    public static void insertAccount(String json) {
        insertAccount(Document.parse(json));
    }
    @NonNull
    public static void insertAccount(Document document) {
        account.insertOne(document);
    }
    public static void dropAccount() {
        account.drop();
    }
    
    @NonNull
    public static void insertAssets(JSONObject json) {
        insertAssets(json.toString());
    }
    @NonNull
    public static void insertAssets(String json) {
        insertAssets(Document.parse(json));
    }
    @NonNull
    public static void insertAssets(Document document) {
        assets.insertOne(document);
    }
    public static void dropAssets() {
        assets.drop();
    }

    @NonNull
    public static void insertBlock(JSONObject json) {
        insertBlock(json.toString());
    }
    @NonNull
    public static void insertBlock(String json) {
        insertBlock(Document.parse(json));
    }
    @NonNull
    public static void insertBlock(Document document) {
        block.insertOne(document);
    }
    public static void dropBlock() {
        block.drop();
    }
    
    public static Document createNewBlock() {
        var latest = block.find().limit(1).sort(new Document("_id", -1)).first();
        var number = 0;
        var preHash = "0000000000";
        if (latest != null) {
            var tempN = latest.get("number", Integer.class);
            if (tempN != null) {
                number = tempN + 1;
            }
            var tempH = latest.get("hash", String.class);
            if (tempH != null && !tempH.isEmpty()) {
                preHash = tempH;
            }
        }

        var merkelPart = new Document("number", number)
            .append("root", "")
            .append("list", new Document[]{});
        var header = new Document("number", number)
            .append("preHash", preHash);
        insertAccount(merkelPart);
        insertAssets(merkelPart);
        insertTransaction(merkelPart);
        insertBlock(header);
        return new Document("number", number).append("preHash", preHash);
    }

    public static long insertTrans(long number, Document trans) {
        return insertTrans(number, Collections.singletonList(trans));
    }
    
    public static long insertTrans(long number, List<Document> trans) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$pushAll", new Document("list", trans));
        return transaction.updateOne(filter, insert).getModifiedCount();
    }
    
    public static List<Document> getTransFromBlock(long number) {
        var filter = Filters.eq("number", number);
        var block = transaction.find(filter).limit(1).first();
        return block == null ? new ArrayList<>() : block.getList("list", Document.class);
    }

    public static long insertAssets(long number, Document ass) {
        return insertAssets(number, Collections.singletonList(ass));
    }
    
    public static long insertAssets(long number, List<Document> ass) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$pushAll", new Document("list", ass));
        return assets.updateOne(filter, insert).getModifiedCount();
    }

    public static List<Document> getAssetsFromBlock(long number) {
        var filter = Filters.eq("number", number);
        var block = assets.find(filter).limit(1).first();
        return block == null ? new ArrayList<>() : block.getList("list", Document.class);
    }
    
    public static long insertAccount(long number, List<Document> acc) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$pushAll", new Document("list", acc));
        return account.updateOne(filter, insert).getModifiedCount();
    }
    
    public static long insertBlock(long number, Document header) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$set", header);
        return account.updateOne(filter, insert).getModifiedCount();
    }
    
}
