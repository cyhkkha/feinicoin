package name.feinimouse.simplecoin.block;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.NonNull;
import org.bson.Document;
import org.json.JSONObject;


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
}
