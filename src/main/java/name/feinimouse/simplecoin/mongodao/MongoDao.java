package name.feinimouse.simplecoin.mongodao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.NonNull;
import org.bson.Document;

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
    protected static MongoCollection<Document> transaction;
    protected static MongoCollection<Document> account;
    protected static MongoCollection<Document> assets;
    protected static MongoCollection<Document> block;
    
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
    public static void insertTest(Document bson) {
        db.getCollection("test").insertOne(bson);
    }
    public static void dropTest() {
        db.getCollection("test").drop();
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
            .append("list", new ArrayList<>());
        var header = new Document("number", number)
            .append("preHash", preHash);
        account.insertOne(merkelPart);
        assets.insertOne(merkelPart);
        transaction.insertOne(merkelPart);
        block.insertOne(header);
        return new Document("number", number).append("preHash", preHash);
    }

    
    public static void insertHeader(long number, Document header) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$set", header);
        block.updateOne(filter, insert).getModifiedCount();
    }
    
}
