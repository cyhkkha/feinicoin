package name.feinimouse.simplecoin.block;

import lombok.Getter;
import lombok.NonNull;
import name.feinimouse.feinicoin.account.Assets;
import name.feinimouse.feinicoin.account.Sign;
import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Hashable;
import name.feinimouse.simplecoin.SimpleSign;
import org.bson.Document;

public class SimpleHashObj implements Hashable {
    private String content;
    @Getter
    private Sign sign;
    @Getter
    private String hash;
    
    public SimpleHashObj(@NonNull Transaction t) {
        this(
            t.getSummary(),
            t.getHash(),
            t.getSign()
        );
    }
    public SimpleHashObj(@NonNull Assets a) {
        this(
            a.getSummary(),
            a.getHash(),
            new SimpleSign()
        );
    }
    
    public SimpleHashObj(String content, String hash, Sign sign) {
        this.content = content;
        this.hash = hash;
        this.sign = sign;
    }
    public String get() {
        return content;
    }
    
    public Document toDocument() {
        return new Document("hash", hash)
            .append("sign", ((SimpleSign)sign).toDoc())
            .append("content", Document.parse(content));
    }
}
