package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.base.HashObj;
import name.feinimouse.feinicoinplus.core.base.BaseObj;
import name.feinimouse.feinicoinplus.core.base.MerkelObj;

public interface HashGen {
    String hash(String content);
    <T extends BaseObj> String hash(T obj);
    <T extends BaseObj> HashObj<T> hashObj(T obj);
    <T extends BaseObj> MerkelObj<T> hashObj(T[] objArr);
}
