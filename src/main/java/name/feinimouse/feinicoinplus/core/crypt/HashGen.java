package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.JsonFormat;

public interface HashGen {
    String hash(String content);
    String hash(JsonFormat t);
    HashObj genHashObj(JsonFormat t);
}
