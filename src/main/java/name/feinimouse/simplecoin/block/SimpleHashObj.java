package name.feinimouse.simplecoin.block;

import lombok.Getter;
import name.feinimouse.feinicoin.block.Hashable;

public class SimpleHashObj implements Hashable {
    private String content;
    @Getter
    private String hash;
    public SimpleHashObj(String content, String hash) {
        this.content = content;
        this.hash = hash;
    }
    public String get() {
        return content;
    }
}
