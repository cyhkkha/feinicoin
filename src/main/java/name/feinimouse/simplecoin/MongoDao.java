package name.feinimouse.simplecoin;

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
    }
    @NonNull
    public static void insertTransaction(JSONObject json) {
        insertTransaction(json.toString());
    }
    @NonNull
    public static void insertTransaction(String json) {
        transaction.insertOne(Document.parse(json));
    }
    public static void dropTransaction() {
        transaction.drop();
    }
    @NonNull
    public static void insertAccount(String json) {
        account.insertOne(Document.parse(json));
    }
    @NonNull
    public static void insertAccount(JSONObject json) {
        insertAccount(json.toString());
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
        assets.insertOne(Document.parse(json));
    }
    public static void dropAssets() {
        assets.drop();
    }
}
