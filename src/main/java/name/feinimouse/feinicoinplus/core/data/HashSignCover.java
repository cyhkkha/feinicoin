package name.feinimouse.feinicoinplus.core.data;

public interface HashSignCover {
    String getSign(String signer);

    String deleteSign(String signer);

    HashSignCover putSign(String signer, String sign);

    boolean excludeSign(String signer);

    int signSize();

    String getHash();

    Object obj();
    
    Class<?> objClass();
}
