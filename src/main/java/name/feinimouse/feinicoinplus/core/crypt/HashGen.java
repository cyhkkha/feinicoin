package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.obj.HashObj;
import name.feinimouse.feinicoinplus.core.obj.OrdinaryObj;
import name.feinimouse.feinicoinplus.core.obj.SummaryObj;

public interface HashGen {
    String hash(String content);
    String hash(SummaryObj obj);
    HashObj genHashObj(OrdinaryObj obj);
    HashObj genHashObj(HashObj[] objArr);
}
