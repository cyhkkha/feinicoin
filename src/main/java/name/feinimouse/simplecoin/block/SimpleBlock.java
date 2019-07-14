package name.feinimouse.simplecoin.block;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.Header;
import name.feinimouse.feinicoin.block.MerkelTree;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleBlock implements Block {
    @Getter @Setter
    private SimpleMerkelTree<SimpleHashObj> accounts;
    @Getter @Setter
    private SimpleMerkelTree<SimpleHashObj> assets;
    @Getter @Setter
    private SimpleMerkelTree<SimpleHashObj> transactions;
    @Getter @Setter
    private SimpleHeader header;
    
    public SimpleBlock(
        SimpleMerkelTree<SimpleHashObj> accounts,
        SimpleMerkelTree<SimpleHashObj> assets,
        SimpleMerkelTree<SimpleHashObj> transactions,
        SimpleHeader header) {
        this.accounts = accounts;
        this.assets = assets;
        this.transactions = transactions;
        this.header = header;
    }
    
    public List<Document> getAccountDocs() {
        return accounts.getList().stream()
            .map(SimpleHashObj::toDocument)
            .collect(Collectors.toList());
}
    public List<Document> getTransDocs() {
        return transactions.getList().stream()
            .map(SimpleHashObj::toDocument)
            .collect(Collectors.toList());
    }
    public List<Document> getAssetDocs() {
        return assets.getList().stream()
            .map(SimpleHashObj::toDocument)
            .collect(Collectors.toList());
    }
    
}
