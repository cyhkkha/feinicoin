package name.feinimouse.simplecoin.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.Arrays;
import java.util.Collections;

/**
 * Create by 菲尼莫斯 on 2019/7/3
 * Email: cyhkkha@gmail.com
 * File name: mongoDao
 * Program : feinicoin
 * Description :
 */
public class mongoDao {
    private static final String USERNAME = "root";
    private static final String DATABASE = "test";
    private static final char[] PASSWORD = "12345".toCharArray();
    private static final String COLLECTION = "simple-block";
    private static final int MaxConnect = 30;
    private static final int MaxWaitThread = 30;
    private static final int MaxTimeOut = 60;
    private static final int MaxWaitTime = 60;
    
    
    private static MongoClient mongoClient;
    static {
        var credential = MongoCredential.createCredential(USERNAME, DATABASE, PASSWORD);
        var address = new ServerAddress("localhost", 27017);
        var builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(MaxConnect)
            .threadsAllowedToBlockForConnectionMultiplier(MaxWaitThread)
            .connectTimeout(MaxTimeOut * 1000)
            .maxWaitTime(MaxWaitTime *1000);
        var option = builder.build();
        mongoClient = new MongoClient(address, credential, option);
    }
    
}
