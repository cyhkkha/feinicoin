package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.base.HashObj;
import name.feinimouse.feinicoinplus.core.base.OrdinaryObj;
import name.feinimouse.feinicoinplus.core.base.SummaryAble;

public interface HashGen {
    String hash(String content);
    String hash(SummaryAble obj);
    <T extends OrdinaryObj> HashObj<T> genHashObj(T obj);
    <T> HashObj<HashObj<T>[]> genHashObj(HashObj<T>[] objArr);
}
