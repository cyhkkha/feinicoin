package name.feinimouse.feinicoinplus.core;

public interface HashGen {
    String hash(String content);
    <T> HashObj hashObj(T obj, String summary);
    <T> HashObj hashObj(T[] objArr, String[] summaryArr);
}
