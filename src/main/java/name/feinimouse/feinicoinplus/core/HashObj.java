package name.feinimouse.feinicoinplus.core;


public interface HashObj<T> extends BaseObj {
    String gainHash();
    String summary();
    T obj();
}
