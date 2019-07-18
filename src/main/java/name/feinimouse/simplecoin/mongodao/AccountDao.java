package name.feinimouse.simplecoin.mongodao;

import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

public class AccountDao extends MongoDao {
    public static void insertList(long number, List<Document> acc) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$push", new Document("list", new Document("$each", acc)));
        account.updateOne(filter, insert).getModifiedCount();
    }
    public static void setRoot(long number, String root) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$set", new Document("root", root));
        account.updateOne(filter, insert);
    }
}
