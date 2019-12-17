package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.block.HashObj;
import name.feinimouse.feinicoinplus.core.block.Jsobj;

public interface HashGen {
    String hash(String content);
    String hash(Jsobj t);
    HashObj genHashObj(Jsobj t);
}
