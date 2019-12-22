package name.feinimouse.feinicoinplus.core.crypt;

import name.feinimouse.feinicoinplus.core.HashObj;
import name.feinimouse.feinicoinplus.core.BaseObj;

public interface HashGen {
    String hash(String content);
    <T extends BaseObj> HashObj<T> hashObj(T obj, String summary);
    <T extends BaseObj> HashObj<T[]> hashObj(T[] objArr, String[] summaryArr);
}
