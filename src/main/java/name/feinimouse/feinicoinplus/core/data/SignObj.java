package name.feinimouse.feinicoinplus.core.data;

public interface SignObj {
    String getSign(String signer);

    String deleteSign(String signer);

    SignObj putSign(String signer, String sign);

    boolean excludeSign(String signer);

    int signSize();
}
