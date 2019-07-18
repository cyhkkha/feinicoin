package name.feinimouse.simplecoin.mongodao;

import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssetsDao extends MongoDao {
    public static void insertList(long number, Document ass) {
        insertList(number, Collections.singletonList(ass));
    }

    public static void insertList(long number, List<Document> ass) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$push", new Document("list", new Document("$each", ass)));
        assets.updateOne(filter, insert).getModifiedCount();
    }

    @SuppressWarnings("unchecked")
    public static List<Document> getList(long number) {
        var filter = Filters.eq("number", number);
        var block = assets.find(filter).limit(1).first();
        return block == null ? new ArrayList<>() : (List<Document>) block.get("list");
    }
    
    public static void setRoot(long number, String root) {
        var filter = Filters.eq("number", number);
        var insert = new Document("$set", new Document("root", root));
        assets.updateOne(filter, insert);
    }
}
