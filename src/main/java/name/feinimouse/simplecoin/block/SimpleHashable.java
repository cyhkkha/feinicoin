package name.feinimouse.simplecoin.block;

import name.feinimouse.feinicoin.block.Hashable;
import org.bson.Document;
import org.json.JSONObject;

public class SimpleHashable implements Hashable {
    private String hash;
    public SimpleHashable(Document d) {
        this(d.getString("hash"));
    }
    public SimpleHashable(JSONObject j) {
        this(j.getString("hash"));
    }
    public SimpleHashable(String hash) {
        this.hash =hash;
    }
    @Override
    public String getHash() {
        return hash;
    }
}
